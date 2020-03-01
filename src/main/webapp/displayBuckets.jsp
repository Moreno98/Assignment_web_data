<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- 
    Document   : displayBuckets
    Created on : Feb 27, 2020, 3:25:17 PM
    Author     : moren
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Buckets</title>
        <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    </head>
    <body>
        <h2 class="text-center">Buckets frequency</h2>
        <h4 class="text-center">${requestScope.message}</h4>
        <div id="chart"></div>
        <hr>
        <div id="pie" class="text-center"></div>
    </body>
    <script>
        window.onload = function() {
            //Bar chart:
            var options = {
                series: [{
                    data: <%=request.getAttribute("values")%>
                }],
                chart: {
                    type: 'bar',
                    height: 800
                },
                plotOptions: {
                  bar: {
                    vertical: true
                  }
                },
                dataLabels: {
                  enabled: false
                },
                xaxis: {
                    categories: <%=request.getAttribute("keys")%>
                },lines: {
                    show: true
                }
            };
            var chart = new ApexCharts(document.querySelector("#chart"), options);
            chart.render();

            //Pie chart
            var values = <%=request.getAttribute("percentages")%>;
            console.log(values);
            var keys = <%=request.getAttribute("keys")%>;
            var dataPoints = [];
            var i = 0;
            //Create the specific format for pie chart
            while(i < keys.length) {
                var j = {"label": keys[i], "y": values[i]};
                dataPoints.push(j);
                i++;
            }
            
            var chart = new CanvasJS.Chart("pie", {
                animationEnabled: true,
                title: {
                        text: "Pie Chart"
                },
                data: [{
                        type: "pie",
                        startAngle: 240,
                        yValueFormatString: "##0.00\"%\"",
                        indexLabel: "{label} {y}",
                        dataPoints: dataPoints
                }]
            });
            chart.render();
        
    };
        
    </script>
    <script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
    <script src="canvasjs.min.js"></script>
</html>
