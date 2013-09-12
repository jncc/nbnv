<table cellpadding="0" cellspacing="0" border="0" class="display" id="example">
  <thead>
    <tr>
      <th>Organisation</th>
      <th>Location</th>
      <th>Site Name</th>
      <th>Start Date</th>
      <th>End Date</th>
      <th>Recorder</th>
    </tr>
  </thead>
  <tbody>
    <% _.each(this, function(observation){ %>
      <tr>
        <td><a href="<%=observation.dataset.href%>" target="_blank"><%=observation.dataset.organisationName%></a></td>
        <td><%=location%></td>
        <td><%=siteName%></td>
        <td><%=startDate%></td>
        <td><%=endDate%></td>
        <td><%=recorder%></td>
      </tr>
    <%});%>
  </tbody>
</table>