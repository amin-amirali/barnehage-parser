# barnehage-parser

A project to gather Oslo Kommune's public information on barnehages in the Oslo area.

Are you in the process of choosing a barnehage in Oslo, but are missing access to the right tool to help you decide?
With so many barnehages in my own area, it was specially hard for me to put the info of each one of them side-by-side. I wanted to answer questions such as:
- Which ones have the best rating?
- Which ones have the longest opening hours?
- How do they fare against each other in terms of ratings from parents?

All of this information is already available in the barnehage's page from the Oslo Kommune, but nothing is there to help me choose.

With this application, you will have access to all of this information put into a comma-separated file, which you can then import into your favourite analysis tool, such as Google Sheets.

A sample of the output:
```
name,address,url,ages,total_price,food_price,food_description,barnets_utvikling,barnets_trivsel,total_tilfredshet,area_per_kid,kids_count,ute__og_innemiljo,informasjon,svarprosent,kids_per_employee,pct_employees_with_degree,vacation_weeks,opening_hours
Abildsø menighetsbarnehage,Langerudhaugen 1 1187 OSLO,https://www.oslo.kommune.no/barnehage/finn-barnehage-i-oslo/abildso-menighetsbarnehage/,1 år - 6 år,"3000,-","170,-",,"4,6","4,6","4,4","6,2",33,,"4,3",50%,6,"14,3",,07:00-17:00
Akersbakken barnehage,Akersbakken 27 0172 OSLO,https://www.oslo.kommune.no/barnehage/finn-barnehage-i-oslo/akersbakken-barnehage/,1 år - 6 år,"3000,-","200,-",2-3 måltider i barnehagen. frukt og grønt,"4,3","4,4","4,1","4,9",54,,"3,9",54%,"5,4","46,2",,07:30-17:00
Akersløkka barnehage,Torvbakkgata 9 0550 OSLO,https://www.oslo.kommune.no/barnehage/finn-barnehage-i-oslo/akerslokka-barnehage/,10 måneder - 6 år,"3000,-","200,-",,"4,6","4,6","4,3","4,3",60,,"4,4",90%,"5,9","43,8",,07:30-17:00
Akersveien Kanvas-barnehage,Westye Egebergs gate 11 0172 OSLO,https://www.oslo.kommune.no/barnehage/finn-barnehage-i-oslo/akersveien-kanvas-barnehage/,10 måneder - 6 år,"3000,-","300,-","2 til 3 varmmåltider i uken, brød med pålegg 2 ganger i uken og frukt og grønt til ettermiddagsmåltid.","4,6","4,7","4,7",5,54,,"4,3",57%,6,50,,07:30-17:00
Allèen barnehage,Fyrstikkalleen 19 0661 OSLO,https://www.oslo.kommune.no/barnehage/finn-barnehage-i-oslo/alleen-barnehage/,1 år - 6 år,"3000,-","520,-",Barna får servert varm mat laget fra bunnen hver dag.,"3,8","4,2","3,8","5,8",63,,"4,1",54%,"5,9","42,4",,07:00-17:00
```

Some neat advantages of using Google Sheets with this data: since it includes an address, you can use Google Maps API to calculate commute time to/from your own place. Have a look at the following link for more details on how to use it: https://www.labnol.org/google-maps-sheets-200817

# Build
```lein uberjar```

# Run
```java -jar target/barnehage-parser.jar```
