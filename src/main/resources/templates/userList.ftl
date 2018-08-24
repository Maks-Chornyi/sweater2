<#import "parts/common.ftl" as c>
<@c.page>
    List of Users
<table>
    <thead>
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th></th>
        </tr>
    </thead>
    <tbody>


    <#list users as user>
        <tr>
            <td>${user.username}</td>
            <td><#list user.roles as role>${role}<#sep>, </#list></td>
            <td><a href="/user/${user.id}">Edit</a> </td>
        </tr>
    </#list>
    </tbody>
</table>
<div></div>
<div><a href="/main">Back to Main page</a></div>
</@c.page>