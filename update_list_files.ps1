Get-ChildItem -Path 'src/main/resources/templates' -Recurse -Filter 'list.html' | ForEach-Object { Write-Host "Processing "; (Get-Content .FullName) -replace '<nav class=\
sidebar\>[\s\S]*?<\/nav>\s*<nav class=\navbar
bg-white\>[\s\S]*?<\/nav>', '<!-- одключение боковой панели навигации -->
<div th:replace="~{fragments/navbar :: sidebar}"></div>

<!-- одключение верхней панели навигации -->
<div th:replace="~{fragments/navbar :: topbar}"></div>' | Set-Content .FullName }
