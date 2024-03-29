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
    <link rel="stylesheet" href="css/dungeonmaker.css">
    <link rel="stylesheet" href="css/dungeonmaptiles.css" title="tiles">
    <link rel="stylesheet" href="css/tooltipsmall.css">
</head>
<body>

${content}
<!-- Again, we're serving up the unminified source for clarity. -->
<script src="js/jquery-3.1.1.js"></script>
<script src="js/dungeonGeneration.js"></script>
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
     dealing with real world issues like old browsers.  -->
</html>
