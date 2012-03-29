<html>
    <head>
        <title>Flickr On Dynamo</title>
    </head>
    <body>
        <center>
            <h1>Flickr On Dynamo</h1>
            <h2><i>Search and Like</i> Recent <i>Photos on Flickr</i></h2>
            <p>Powered by
            <a href="http://www.flickr.com/">Flickr</a>
            and
            <a href="http://aws.amazon.com/dynamodb">Amazon DynamoDB</a></p>
            <g:set var="sampleImage" value="${resource(dir:'images', file:'toronto-in-autumn.JPG')}"></g:set>
            <a href="http://www.flickr.com/photos/74871381@N03/6737840301/">
                <img src="${sampleImage}" width="100" height="75" border="0" alt="Toronto in autumn by WC"/>
            </a>
            <form>
                <table>
                    <tr/>
                    <tr/>
                    <tr>
                        <td><label><i>author</i>: </label></td>
                        <td align="left">
                            <input name="authorName" value="${authorName}">
                        </td>
                    </tr>
                    <tr>
                        <td><label><i>published from:</i></label></td>
                        <td align="left">
                            <g:set var="yesterday" value="${new Date() - 1}"></g:set>
                            <g:datePicker name="publishedFrom" value="${publishedFrom ?: yesterday}"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="center"><label><i>to:</i></label></td>
                        <td align="left">
                            <g:set var="tomorrow" value="${new Date() + 1}"></g:set>
                            <g:datePicker name="publishedTo" value="${publishedTo ?: tomorrow}"/>
                        </td>
                    </tr>
                    <tr>
                        <td><label><i>tag</i>:</label></td>
                        <td align="left">
                            <input name="category" value="${category}">
                            <label style="font-size: small; color: gray;"></label>
                        </td>
                    </tr>
                    <tr>
                        <td><label><i>title</i>:</label></td>
                        <td align="left">
                            <input name="titlePhrase" value="${titlePhrase}">
                            <label style="font-size: small; color: gray;"><i>search titles containing this</i></label>
                        </td>
                    </tr>
                    <tr>
                        <td><label><i>limit:</i></label></td>
                        <td align="left">
                            <input name="limit" size="2" value="${limit ?: 20}">
                            <label><i>photos max</i></label>
                            <label style="font-size: small; color: gray;"><i>capped at 100</i></label>
                        </td>
                    </tr>
                </table>
                <g:actionSubmit value="search" action="search"/>
                <g:checkBox name="likesOnly" checked="${likesOnly}"/><label style="font-size: small;"><i>likes only</i></label>
                <br/>
                <g:link action="clear">clear</g:link>
                <g:if test="${entries}">
                    <h3 style="color: green;">photos found</h3>
                    <table>
                        <g:set var="entriesShown" value="${0}"></g:set>
                        <g:each var="entry" in="${entries}">
                            <g:if test="${entriesShown % 4 == 0}">
                                <tr>
                            </g:if>
                                    <td valign="top">
                                        <div align="center">
                                            <a href="${entry.imageLink}">
                                                <img align="top" src="${entry.imageLink}" width="100" height="75" border="0" alt="${entry.title}"/>
                                            </a>
                                        </div>
                                        <div align="center">
                                            <a href="${entry.pageLink}">
                                                ${entry.title ?: 'no title'}
                                            </a>
                                        </div>
                                        <div align="center">
                                            <g:set var="encodedPrimaryKey" value="like|${entry.published.time}|${entry.authorName}"></g:set>
                                            <g:radio name="${encodedPrimaryKey}" value="${encodedPrimaryKey}"/>Like &#9825;
                                        </div>
                                        <div align="center">
                                            by <b>${entry.authorName}</b>
                                        </div>
                                        <div align="center" style="font-size: small;">
                                            Likes: ${entry.likes ?: 0}
                                        </div>
                                        <g:if test="${entry.dateTaken}">
                                            <div align="center" style="font-size: small;">
                                                taken <i>${java.text.DateFormat.instance.format(entry.dateTaken)}</i>
                                            </div>
                                        </g:if>
                                        <g:if test="${entry.categories}">
                                            <div align="center" style="font-size: small;">
                                                <i>${entry.categories}</i>
                                            </div>
                                        </g:if>
                                    </td>
                            <g:set var="entriesShown" value="${entriesShown + 1}"></g:set>
                            <g:if test="${entriesShown % 4 == 0}">
                                </tr>
                                <tr/>
                                <tr/>
                                <tr/>
                            </g:if>
                        </g:each>
                    </table>
                    <g:actionSubmit value="register likes" action="registerLikes"/>
                </g:if>
            </form>
        </center>
    </body>
</html>
