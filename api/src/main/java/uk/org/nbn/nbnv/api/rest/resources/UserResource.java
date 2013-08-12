package uk.org.nbn.nbnv.api.rest.resources;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.authentication.TokenResetCredentials;
import uk.org.nbn.nbnv.api.authentication.ExpiredTokenException;
import uk.org.nbn.nbnv.api.authentication.InvalidCredentialsException;
import uk.org.nbn.nbnv.api.authentication.InvalidTokenException;
import uk.org.nbn.nbnv.api.authentication.Token;
import uk.org.nbn.nbnv.api.authentication.TokenAuthenticator;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.UserAuthenticationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.UserMapper;
import uk.org.nbn.nbnv.api.mail.TemplateMailer;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;

/**
 *
 * @author Chris Johnson
 */
@Component
@Path("/user")
public class UserResource extends AbstractResource {

    private static final String STRING_ENCODING = "UTF-8";
    private final int tokenTTL;
    private final String tokenCookieKey;
    private final String domain;
    @Autowired TokenAuthenticator tokenAuth;
    @Autowired TokenResetCredentials credentialsResetter;
    @Autowired UserMapper userMapper;
    @Autowired OrganisationMapper organisationMapper;
    @Autowired OperationalOrganisationMapper oOrganisationMapper;
    @Autowired OperationalUserMapper oUserMapper;
    @Autowired UserAuthenticationMapper userAuthenticationMapper;
    @Autowired TemplateMailer mailer;

    @Autowired
    public UserResource(Properties properties) throws NoSuchAlgorithmException {
        tokenTTL = Integer.parseInt(properties.getProperty("sso_token_default_ttl"));
        tokenCookieKey = properties.getProperty("sso_token_key");
        domain = properties.getProperty("sso_token_domain");
    }

    /**
     * Return details about the current user from the data warehouse
     * 
     * @param user The current user
     * 
     * @return The current user
     * 
     * @response.representation.200.qname User
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getDetails(@TokenUser User user) {
        return user;
    }

    /**
     * Return full up to date details about the current user from the core 
     * database
     * 
     * @param user The current user
     * 
     * @return The most upto date data about the current user
     * 
     * @response.representation.200.qname User
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/full")
    @Produces(MediaType.APPLICATION_JSON)
    public User getFullDetails(@TokenUser(allowPublic = false) User user) {
        return oUserMapper.getUserById(user.getId());
    }

    /**
     * Log the indicated user into the system
     * 
     * @param username The username of the user
     * @param password The password of the user
     * @param remember Whether the browser should remember this data (stay 
     * logged in)
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws InvalidCredentialsException Credentials are invalid (incorrect
     * username / password)
     * @throws InvalidTokenException Token is invalid
     * @throws ExpiredTokenException Token has expired
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTokenCookie(
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @DefaultValue("false") @QueryParam("remember") Boolean remember) throws InvalidCredentialsException, InvalidTokenException, ExpiredTokenException {
        Token token = tokenAuth.generateToken(username, password, tokenTTL);

        Map<String, Object> toReturn = new HashMap<String, Object>();
        toReturn.put("success", true);
        toReturn.put("user", tokenAuth.getUser(token));
        toReturn.put("token", token.getBytes());

        return Response.ok(toReturn)
                .cookie(new NewCookie(
                tokenCookieKey,
                Base64.encodeBase64URLSafeString(token.getBytes()),
                "/", domain, "authentication token",
                (remember) ? tokenTTL / 1000 : NewCookie.DEFAULT_MAX_AGE, false))
                .build();
    }

    /**
     * Change the current users password
     * 
     * @param user The current user
     * @param password The new password to change to
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @PUT
    @Path("/passwords/change")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setUserPassword(
            @TokenUser(allowPublic = false) User user,
            @QueryParam("password") String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1"), md5 = MessageDigest.getInstance("MD5");
        byte[] passwordHashSHA1 = sha1.digest(password.getBytes(STRING_ENCODING));
        byte[] md5HashSHA1 = sha1.digest(md5.digest(password.getBytes(STRING_ENCODING)));

        oUserMapper.setUserPassword(user, passwordHashSHA1, md5HashSHA1);
        return Response.ok("success").build();
    }

    /**
     * Change the current users password from the user panel
     * 
     * @param user The current user
     * @param password The new password
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/passwords/change")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setUserPasswordPost(
            @TokenUser(allowPublic = false) User user,
            @FormParam("password") String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return setUserPassword(user, password);
    }

    /**
     * Change password from the forgotten password interface
     * 
     * @param username The requesting users username
     * @param resetToken The reset token from the forgotten password email
     * @param password The new password
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws UnsupportedEncodingException
     * @throws InvalidTokenException
     * @throws ExpiredTokenException
     * @throws NoSuchAlgorithmException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/passwords/change/{username}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setUserPassword(
            @PathParam("username") String username,
            @FormParam("reset") String resetToken,
            @FormParam("password") String password) throws UnsupportedEncodingException, InvalidTokenException, ExpiredTokenException, NoSuchAlgorithmException {
        return setUserPassword(credentialsResetter.getUser(
                username, new Token(Base64.decodeBase64(resetToken))), password);
    }

    /**
     * Email a password reset form to a user with the specified email address
     * 
     * @param emailAddress The email address of the user who has forgotten their
     * password
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws JSONException
     * @throws InvalidCredentialsException
     * @throws IOException
     * @throws TemplateException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/mail/password")
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestPasswordReset(String emailAddress) throws JSONException, InvalidCredentialsException, IOException, TemplateException {
        User user = userMapper.getUserFromEmail(emailAddress);
        if (user != null) {
            Token generateToken = credentialsResetter.generateToken(user.getUsername(), tokenTTL);
            //email the user with the activation key
            Map<String, Object> message = new HashMap<String, Object>();
            message.put("name", user.getForename());
            message.put("portal", properties.getProperty("portal_url"));
            message.put("token", Base64.encodeBase64URLSafeString(generateToken.getBytes()));
            mailer.send("forgotten-password.ftl", user.getEmail(), "NBN Gateway: Your password reset link", message);

            return Response.ok(new JSONObject()
                    .put("success", true)
                    .put("status", "A password reset command has been e-mailed to you")).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(new JSONObject()
                    .put("success", false)
                    .put("status", "No user is known with this username")).build();
        }
    }

    /**
     * Email the users username to their email address from the forgotten 
     * username form
     * 
     * @param emailAddress The users email address
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws JSONException
     * @throws IOException
     * @throws TemplateException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/mail/username")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsername(String emailAddress) throws JSONException, IOException, TemplateException {
        User user = userMapper.getUserFromEmail(emailAddress);
        if (user != null) {
            //email the user with their username
            Map<String, Object> message = new HashMap<String, Object>();
            message.put("name", user.getForename());
            message.put("username", user.getUsername());
            mailer.send("forgotten-username.ftl", user.getEmail(), "NBN Gateway: Your username reminder", message);

            return Response.ok(new JSONObject()
                    .put("success", true)
                    .put("status", "Your username has been sent to you via e-mail.")).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(new JSONObject()
                    .put("success", false)
                    .put("status", "No user is known with this email")).build();
        }
    }

    /**
     * Logout the current user
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response destroyTokenCookie() {
        Map<String, Object> toReturn = new HashMap<String, Object>();
        toReturn.put("success", true);
        toReturn.put("user", User.PUBLIC_USER);

        return Response.ok(toReturn)
                .cookie(new NewCookie(tokenCookieKey, null, "/", domain, null, 0, false))
                .build();
    }

    /**
     * Register a new user to the NBN gateway
     * 
     * @param newUser The details of the new user to put into the database
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws TemplateException
     * @throws JSONException
     * @throws NoSuchAlgorithmException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerNewUser(@Valid User newUser) throws
            UnsupportedEncodingException, IOException, TemplateException, JSONException, NoSuchAlgorithmException {
        //Perform some checks to before hitting database constraints. 
        //Would be better to read the status from a constraint violation
        //and report on this     
        if (userMapper.getUser(newUser.getUsername()) != null) {
            throw new IllegalArgumentException("The specified username is already taken");
        } else if (userMapper.getUserFromEmail(newUser.getEmail()) != null) {
            throw new IllegalArgumentException("The specified e-mail address is already registered to another user.");
        } else {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1"), md5 = MessageDigest.getInstance("MD5");
            byte[] passwordHashSHA1 = sha1.digest(newUser.getPassword().getBytes(STRING_ENCODING));
            byte[] md5HashSHA1 = sha1.digest(md5.digest(newUser.getPassword().getBytes(STRING_ENCODING)));
            String activationKey = RandomStringUtils.randomAlphanumeric(12); //generate a random one off activation key
            //save that key in the database
            oUserMapper.registerNewUser(newUser, Calendar.getInstance().getTime(),
                    activationKey, passwordHashSHA1, md5HashSHA1);
            //email the user with the activation key
            Map<String, Object> message = new HashMap<String, Object>();
            message.put("name", newUser.getForename());
            message.put("portal", properties.getProperty("portal_url"));
            message.put("activationKey", activationKey);
            message.put("username", newUser.getUsername());
            mailer.send("activation.ftl", newUser.getEmail(), "NBN Gateway: Please activate your account", message);

            return Response.ok(new JSONObject()
                    .put("success", true)
                    .put("status", "An activation code has been sent you your e-mail.")).build();
        }
    }

    /**
     * Activate a newly registered user account
     * 
     * @param username The username of the account
     * @param activationCode The activation code sent to the user
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws JSONException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @PUT
    @Path("/activations/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateUser(@PathParam("username") String username, String activationCode) throws JSONException {
        if (oUserMapper.activateNewUser(username, activationCode) == 1) {
            return Response.ok(new JSONObject()
                    .put("success", true)
                    .put("status", "activated successfully")).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity(new JSONObject()
                    .put("success", false)
                    .put("status", "The activation code is not valid for the given username")).build();
        }
    }

    /**
     * Return a list of all datasets which the current user has admin rights
     * over
     * 
     * @param user The current user (Must be logged in)
     * 
     * @return A List of Datasets that this user has admin rights over
     * 
     * @response.representation.200.qname List<Dataset>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/adminDatasets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getUserAdminDatasets(@TokenUser(allowPublic = false) User user) {
        return userMapper.getDatasetsUserAdmins(user.getId());
    }

    /**
     * Return a list of organisations of which the current user is a member
     * 
     * @param user The current user (Must be logged in)
     * 
     * @return a List of Organisations that the user is a member of
     * 
     * @response.representation.200.qname List<Organisation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/organisations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Organisation> getUserOrganisations(@TokenUser(allowPublic = false) User user) {
        return organisationMapper.selectByUser(user.getId());
    }

    /**
     * Return a list of organisations of which the current user has admin rights
     * 
     * @param user The current user (Must be logged in)
     * 
     * @return A list of organisations that the user is an admin of
     * 
     * @response.representation.200.qname List<Organisation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/adminOrganisations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Organisation> getUserAdminOrganisations(@TokenUser(allowPublic = false) User user) {
        return organisationMapper.selectByAdminUser(user.getId());
    }

    /**
     * Search for users in the core database, excluding those who are members
     * of the specified organisation
     * 
     * @param user The current user (Must be logged in)
     * @param term The search term (username, first name, last name or email)
     * @param orgId An organisation ID
     * 
     * @return A List of users that match the search term
     * 
     * @response.representation.200.qname List<User>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> searchForUserByPartial(@TokenUser(allowPublic=false) User user, @QueryParam("term") String term, @QueryParam("organisation") int orgId, @QueryParam("dataset") String dataset) {
        if (dataset != null && !dataset.equals("")) {
            return oUserMapper.searchForUserExcludeDatasetAdmins(term, dataset);
        }
       return oUserMapper.searchForUserExcludeOrganisationMembers("%" + term + "%", orgId);
    }

    /**
     * Modify the currently logged in user with new details from the user modify
     * form in the user admin section
     * 
     * @param user The current user (Must be logged in)
     * @param modified A user with modified user details from the user modify 
     * form
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws JSONException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/modify")
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyExistingUser(@TokenUser(allowPublic = false) User user, @Valid User modified) throws JSONException {

        if (!user.getEmail().equals(modified.getEmail())
                && oUserMapper.getUserFromEmail(modified.getEmail()) != null) {
            throw new IllegalArgumentException("The specified e-mail address is already registered to another user.");
        }

        oUserMapper.updateUserDetails(user.getId(), modified.getForename(), modified.getSurname(), modified.getEmail(), modified.getPhone());

        return Response.ok(new JSONObject()
                .put("success", true)
                .put("status", "You have successfully modifed your user details")).build();
    }

// Function no longer in use
//    /**
//     * Change the email subscription settings of the current user from the user
//     * modify panel
//     * 
//     * @param user The current user (Must be logged in)
//     * @param allowEmailAlerts Whether to allow email alerts
//     * @param subscribedToAdminEmails Whether user is subscribed to admin emails
//     * or not
//     * @param subscribedToNBNMarketting Whether user is subscribed to NBN 
//     * Marketing emails or not
//     * 
//     * @return A Response object detailing the success or failure of the action
//     * 
//     * @throws JSONException 
//     * 
//     * @response.representation.200.qname User
//     * @response.representation.200.mediaType application/json
//     */
//    @POST
//    @Path("/emailSettings")
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response modifyEmailSettings(@TokenUser(allowPublic = false) User user, 
//        @FormParam("subscribedToNBNMarketting") int subscribedToNBNMarketting) throws JSONException {
//        oUserMapper.updateUserEmailSettings(user.getId(), subscribedToNBNMarketting);
//        
//        return Response.ok(new JSONObject()
//                .put("success", true)
//                .put("status", "You have successfully modifed your email settings")).build();
//    }
}