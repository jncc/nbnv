<div class="others">
  <% _.each(others, function(other, index){ %>
    <div class="img-polaroid" option="<%=index%>" layer="<%=other%>">
      <div class="<%=other%>-icon"></div>
      <div class="label"><%=other%></div>
    </div>
  <%});%>
</div>

<div class="obvious-alt img-polaroid" layer="<%=obvious%>">
  <div class="<%=obvious%>-icon"></div>
  <div class="label"><%=obvious%></div>
</div>