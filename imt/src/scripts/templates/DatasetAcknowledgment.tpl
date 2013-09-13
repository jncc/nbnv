<% _.each(organisations, function(organisation){ %>
  <h2><%=organisation.name%></h2>
  <ul>
    <% _.each(organisation.datasets, function(dataset){ %>
      <li><a href="<%=dataset.attributes.href%>"><%=dataset.attributes.title%></a></li>
    <%});%>
  </ul>
<%});%>