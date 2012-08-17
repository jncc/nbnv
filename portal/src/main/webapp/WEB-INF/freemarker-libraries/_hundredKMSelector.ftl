<#--The following freemarker template represents a reusable component for
selecting a 100km Grid Square of the British/Irish National grid-->

<#--Define the image start and stop for the grid squares-->
<#assign bng = {
    "x": [62,88,115,141,168,194,221, 247]?reverse,
    "y": [2,29,55,82,108,135,161,188,214,241,267,294,320,347]
}/>

<#--The names of the grid squares which are true squares-->
<#assign gridSquareRows=[
    [   "HL",   "HM",   "HN",   "HO",   "HP",   "JL",   "JM"],
    [   "HQ",   "HR",   "HS",   "HT",   "HU",   "JQ",   "JR"],
    [   "HV",   "HW",   "HX",   "HY",   "HZ",   "JV",   "JW"],
    [   "NA",   "NB",   "NC",   "ND",   "NE",   "OA",   "OB"],
    [   "NF",   "NG",   "NH",   "NJ",   "NK",   "OF",   "OG"],
    [   "NL",   "NM",   "NN",   "NO",   "NP",   "OL",   "OM"],
    [                   "NS",   "NT",   "NU",   "OQ",   "OR"],
    [                   "NX",   "NY",   "NZ",   "OV",   "OW"],
    [                   "SC",   "SD",   "SE",   "TA",   "TB"],
    [                   "SH",   "SJ",   "SK",   "TF",   "TG"],
    [                   "SN",   "SO",   "SP",   "TL",   "TM"],
    [                   "SS",   "ST",   "SU",   "TQ",   "TR"],
    [   "SV",   "SW",   "SX",   "SY",   "SZ",   "TV",   "TW"]
]/>

<#--Define the grid squares which arn't really squares but are polygons-->
<#assign nonSquareGridSquares={
    "A" : "15,166,42,168,39,194,13,192",
    "B" : "42,168,68,170,66,196,39,194",
    "C" : "68,170,87,172,94,181,92,199,66,196",
    "D" : "94,181,111,200,92,199",
    "F" : "13,192,39,194,37,221,11,219",
    "G" : "39,194,66,196,64,223,37,221",
    "H" : "66,196,92,199,90,225,64,223",
    "J" : "92,199,111,200,109,227,90,225",
    "L" : "11,219,37,221,35,247,8,245",
    "M" : "37,221,64,223,61,249,35,247",
    "N" : "64,223,90,225,88,252,61,249",
    "O" : "90,225,109,227,106,253,88,252",
    "Q" : "8,245,35,247,33,274,6,272",
    "R" : "35,247,61,249,59,276,33,274",
    "S" : "61,249,88,252,86,278,59,276",
    "T" : "88,252,106,253,104,279,86,278",
    "V" : "6,272,33,274,31,300,4,298",
    "W" : "33,274,59,276,57,302,31,300",
    "X" : "59,276,86,278,84,304,57,302",
    "Y" : "86,278,104,279,84,304",
    "NR" : "88,161,115,161,115,188,102,188,88,171",
    "SM" : "115,267,115,294,96,294",
    "SR" : "96,294,115,294,115,320,88,320,88,305"
}/>
<p>
<img usemap="#hundredKmSelector" alt="100km map" src="/img/hundredKmSelector.gif" width="260" height="350" />
<map name="hundredKmSelector">
    <#list gridSquareRows as gridSquareRow>
        <#list gridSquareRow?reverse as gridSquare>
            <area alt="${gridSquare}" shape="rect" coords="${bng.x[gridSquare_index]},${bng.y[gridSquareRow_index]},${bng.x[gridSquare_index+1]},${bng.y[gridSquareRow_index+1]}" href="/Site_Report/${gridSquare}"/>
        </#list>
    </#list>
    <#list nonSquareGridSquares?keys as poly>
        <area alt="${poly}" shape="poly" coords="${nonSquareGridSquares[poly]}" href="/Site_Report/${poly}"/>
    </#list>
</map>
</p>