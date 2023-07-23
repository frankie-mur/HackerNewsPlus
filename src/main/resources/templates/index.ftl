<html>
<body>
<h1>Story:</h1>
<#list data.items as item>
    <h2>The item at index ${item?index} is ${item}</h2>
</#list>
</body>
</html>
