'use strict';
var lineChartDataXML = {
			labels:[],
			datasets : [
				{
					label: "EURXML",
					fillColor : "rgba(220,220,220,0.2)",
					strokeColor : "rgba(220,220,220,1)",
					pointColor : "rgba(220,220,220,1)",
					pointStrokeColor : "#fff",
					pointHighlightFill : "#fff",
					pointHighlightStroke : "rgba(220,220,220,1)",
					data:[]
				},
				{
					label: "USDXML",
					fillColor : "rgba(151,187,205,0.2)",
					strokeColor : "rgba(151,187,205,1)",
					pointColor : "rgba(151,187,205,1)",
					pointStrokeColor : "#fff",
					pointHighlightFill : "#fff",
					pointHighlightStroke : "rgba(151,187,205,1)",
					data:[]
					}
			]

		};

var lineChartDataJSON = {
			labels:[],
			datasets : [
				
				{
					label: "EURJSON",
					fillColor : "rgba(120,220,200,0.2)",
					strokeColor : "rgba(120,220,200,1)",
					pointColor : "rgba(220,220,220,1)",
					pointStrokeColor : "#fff",
					pointHighlightFill : "#fff",
					pointHighlightStroke : "rgba(220,220,220,1)",
					data:[]
				},
				{
					label: "USDJSON",
					fillColor : "rgba(5,100,205,0.2)",
					strokeColor : "rgba(5,100,205,1)",
					pointColor : "rgba(151,187,205,1)",
					pointStrokeColor : "#fff",
					pointHighlightFill : "#fff",
					pointHighlightStroke : "rgba(151,187,205,1)",
					data:[]
					}
			]

		};

window.onload = function(){
    var msIn90Days = 7776000000;
    var now= new Date();
    var msNow = now.getTime();
    var msPast =  msNow-msIn90Days;
    var ctx;
	requestData(msPast,msNow);
	ctx = document.getElementById("canvasXML").getContext("2d");
		window.myLine = new Chart(ctx).Line(lineChartDataXML, {
			responsive: true
		});
    ctx = document.getElementById("canvasJSON").getContext("2d");
		window.myLine = new Chart(ctx).Line(lineChartDataJSON, {
			responsive: true
		});
};
	
function getDataJSON(year, month, day,i){
    var xhr;
    try{
        xhr = new XMLHttpRequest();
    }catch(e){
        try{
            xhr=new ActiveXObject("Msxml2.XMLHTTP");
        }catch(e){
            try{
                xhr = new ActiveXObject("Microsoft.XMLHTTP");
            }catch(e){
                alert("Your browser broke!");
                return false;
            }

        }
    }

    xhr.onreadystatechange = function(){
            if (xhr.readyState != 4) return;
            clearTimeout(timeout);
            if (xhr.status == 200) {
                var jsonObj = JSON.parse(xhr.responseText);
                var valutes = jsonObj.ValCurs.valute;
                var str;
                
                for(var valute in valutes){
                        str = valutes[valute].charCode;
                        if(str==="EUR"){
                            lineChartDataJSON.datasets[0].data[i]= valutes[valute].value;
                        }
                        else if(str==="USD"){
                            lineChartDataJSON.datasets[1].data[i]= valutes[valute].value;
                        }    
                }
               
            }
     };
     xhr.open("GET", "JSON/"+year+"/"+month+"/JSON_"+day+".json",false);
     xhr.send();
     var timeout = setTimeout(function () {
            xhr.abort();
        }, 10000);
   
}
function getDataXML(year, month, day,i){
    var xhr;
    try{
        xhr = new XMLHttpRequest();
    }catch(e){
        try{
            xhr=new ActiveXObject("Msxml2.XMLHTTP");
        }catch(e){
            try{
                xhr = new ActiveXObject("Microsoft.XMLHTTP");
            }catch(e){
                alert("Your browser broke!");
                return false;
            }

        }
    }

    xhr.onreadystatechange = function(){
            if (xhr.readyState != 4) return;
            clearTimeout(timeout);
            if (xhr.status == 200) {
                var valutes = xhr.responseXML.getElementsByTagName("Valute");
                var str;
                
                for(var valute in valutes){
                    var valuteValues = valutes[valute].childNodes;
                    for(var valuteValue in valuteValues){
                        str = valuteValues[3].textContent;
                        if(str==="EUR"){
                            lineChartDataXML.datasets[0].data[i]= valuteValues[9].textContent;
                        }
                        else if(str==="USD"){
                            lineChartDataXML.datasets[1].data[i]= valuteValues[9].textContent;
                        }
                    }
                    
                                    
                }
               
            }
        
     };
     xhr.open("GET", "XML/"+year+"/"+month+"/XMl_"+day+".xml",false);
     xhr.send();
     var timeout = setTimeout(function () {
            xhr.abort();
        }, 10000);
   
}
function requestData(from,to){
    var i = 0;
    var msPerDay =86400000;
    for(;from<=to; from+=msPerDay){
        var d = new Date(from);
        var year = d.getFullYear();
        var month = d.getMonth();
        var day =d.getDate();
        lineChartDataXML.labels[i] = day+"."+(month+1)+"."+year;
        lineChartDataJSON.labels[i] = day+"."+(month+1)+"."+year;
        getDataXML(year,(month+1),day,i);
        getDataJSON(year,(month+1),day,i);
        i++;
    }
   
}

   
	
	
