<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- 
    Document   : displayProcessedData
    Created on : Feb 25, 2020, 2:10:39 PM
    Author     : moren
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Processed Data</title>
        <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    </head>
    <body>
        <h2 class="text-center">Word Frequency</h2>
        <h4 class="text-center">${requestScope.message}</h4>
        <!--Div for the chart:-->
        <div id="chart"></div>
    </body>
    <script>
        //Creating the chart based on the processed data from the back-end
        window.onload = function() { 
              var options = {
                series: [{
                    data: <%=request.getAttribute("values")%>//Getting "values" from the back-end
                }],
                chart: {
                    type: 'bar',
                    height: <%=request.getAttribute("length")%>//Getting "length" from the back-end (used to resize the chart)
                },
                plotOptions: {
                  bar: {
                    <%=request.getAttribute("type")%>//Getting "type" from the back-end, it can be horizontal or vertical
                  }
                },
                dataLabels: {
                  enabled: false
                },
                xaxis: {
                        categories: <%=request.getAttribute("keys")%>//Getting "keys" from the back-end
                    },lines: {
                        show: true
                    }
                };
                
                //Create and display the chart:
                var chart = new ApexCharts(document.querySelector("#chart"), options);
                chart.render();
              };
    </script>
    <script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
</html>
