<StyledLayerDescriptor version="1.0.0">
  <NamedLayer>
  <Name><%=layer%></Name>
  <UserStyle>
    <Title><%=layer%></Title>
      <FeatureTypeStyle>
        <Rule>
          <PolygonSymbolizer>
             <Fill>
               <CssParameter name="fill">#<%=colour%></CssParameter>
             </Fill>
             <Stroke>
               <CssParameter name="stroke">#000000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
             </Stroke>
           </PolygonSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>