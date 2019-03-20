<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css">
    <link rel="stylesheet" href="css/bacon.css">
    <link rel="stylesheet" href="../css/bacon.css">
    <link rel="stylesheet" href="../../css/bacon.css">
</head>
<body>

<br><br>
<p id="header"> BACON </p>
${content}
<!-- Again, we're serving up the unminified source for clarity. -->
<script src="js/jquery-2.1.1.js"></script>
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
     dealing with real world issues like old browsers.  -->
</html>