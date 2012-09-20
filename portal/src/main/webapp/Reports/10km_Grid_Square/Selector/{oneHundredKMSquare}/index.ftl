<@template.master title="NBN Gateway - Site Selector for ${URLParameters.oneHundredKMSquare}">
    <h1>100km Square ${URLParameters.oneHundredKMSquare} - Select 10km Square</h1>
    <p class="nbn-navigation">You have selected the 100km square ${URLParameters.oneHundredKMSquare}, 
        click on a 10km square to explore the species recorded in it.
    </p>

    <div style="margin-left:12%; height:600px">
        <div class="tabbed" style="float:left;">
            <h3>Click on a 10km square to select it</h3>
            <img usemap="#tenKmSelector" alt="100km map" src="/img/hundredKmSelector/${URLParameters.oneHundredKMSquare}.gif" width="500" height="500"/>
            <map name="tenKmSelector">
                <#list 0..9 as x>
                    <#list 0..9 as y>
                        <area alt="" shape="rect" coords="${x*50},${y*50},${(x+1)*50},${(y+1)*50}" href="/Reports/10km_Grid_Square/${URLParameters.oneHundredKMSquare}${x}${-(y-9)}"/>
                    </#list>
                </#list>
            </map>
            <div>copyright &copy; Crown copyright and database rights 2011 Ordnance Survey [100017955]</div>
        </div>
        <div class="tabbed" style="float:left">
            <h3>100km Square ${URLParameters.oneHundredKMSquare}</h3>
            <@image_map.hundredKM selected=URLParameters.oneHundredKMSquare/>
        </div>
    </div>
</@template.master>
