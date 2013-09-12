<h5>Which datasets should be used?</h5>
<div class="toggle-container">Toggle datasets:<input type="checkbox"></input></div>
<div class="datasets-container">
  <% _.each(datasets, function(dataset){ %>
    <div>
      <input type="checkbox" value="<%=dataset.key%>" <% if(dataset.selected) {%>checked="checked"<%}%>>
      <span class="title"><em><%=dataset.organisationName%></em> - <%=dataset.title%></span>
    </div>
  <%});%>
</div>