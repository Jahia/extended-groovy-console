import org.jahia.api.Constants
import org.jahia.services.content.JCRCallback
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.services.content.JCRSessionWrapper
import org.jahia.services.content.JCRTemplate
import org.jahia.services.query.QueryWrapper

import javax.jcr.RepositoryException
import javax.jcr.query.Query

final String workspace = Constants.EDIT_WORKSPACE
final Locale locale = null
final String stmt = "select * from [jnt:page] where isdescendantnode('/sites/digitall')"

JCRTemplate.getInstance().doExecuteWithSystemSessionAsUser(null, workspace, locale, new JCRCallback<Object>() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
        QueryWrapper query = session.getWorkspace().getQueryManager().createQuery(
                stmt,
                Query.JCR_SQL2
        )
        query.setLimit(-1)

        for (JCRNodeWrapper node : query.execute().getNodes()) {

            log.info(node.getPath())

        }

        return null
    }
})