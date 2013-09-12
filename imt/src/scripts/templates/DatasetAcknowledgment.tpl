<% _.each(organisations, function(organisation){ %>
  <h2><%=organisation.name%></h2>
  <ul>
    <% _.each(organisation.datasets, function(dataset){ %>
      <li><%=dataset.attributes.title%></li>
    <%});%>
  </ul>
<%});%>