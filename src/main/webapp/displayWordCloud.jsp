<%-- 
    Document   : displayWordCloud
    Created on : Feb 26, 2020, 9:40:26 AM
    Author     : moren
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Word Cloud</title>
        <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
        <script src="https://cdn.anychart.com/releases/v8/js/anychart-base.min.js"></script>
        <script src="https://cdn.anychart.com/releases/v8/js/anychart-tag-cloud.min.js"></script>
        <style>
            html, body, #container {
                width: 100%;
                height: 100%;
                margin: 0;
                padding: 0;
            }
        </style>
    </head>
    <body>
        <h2 class="text-center">Word Cloud</h2>
        <h4 class="text-center">${requestScope.message}</h4>
        <br>
        <div id="container"></div>
        <div class="text-center">
            <a href="index.html" class="btn btn-primary">Home</a>
        </div>
    </body>
    <script>
        anychart.onDocumentReady(function() {
            var values = <%=request.getAttribute("values")%>;
            var keys = <%=request.getAttribute("keys")%>;
            var data = [];
            var i = 0;
            //This cycle creates an array with this format:
            //{"x": key, "value": value}
            //This format is required from the chart library.
            while(i < keys.length) {
                var j = {"x": keys[i], "value": values[i]};
                data.push(j);
                i++;
            }

            // create a tag (word) cloud chart
            var chart = anychart.tagCloud(data);

            // set an array of angles at which the words will be laid out
            chart.angles([0, -45, 45])
            // enable a color range
            chart.colorRange(false);
            // set the color range length
            chart.colorRange().length('80%');
            // display the word cloud chart
            chart.container("container");
            chart.draw();
        });
    </script>
</html>