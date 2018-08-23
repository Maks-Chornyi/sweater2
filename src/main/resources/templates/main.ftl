<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <div>
        <@l.logout />
    </div>
    <div>
        <form method="post">
            <input type="text" name="text" placeholder="Insert message">
            <input type="text" name="tag" placeholder="Insert tag">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit">Add</button>
        </form>
    </div>
    <div>List of Messages</div>
        <#list messages as message>
            <div>
                <b>${message.id}</b>
                <span>${message.text}</span>
                <i>${message.tag}</i>
                <strong>${message.authorName}</strong>
            </div>
        <#else>
            No messages
        </#list>
    <div>
        <form method="post" action="filter">
            <input type="text" name="filter" placeholder="insert tag for filer">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit">Search</button>
        </form>
    </div>
</@c.page>