import org.jahia.api.Constants
import org.jahia.services.content.JCRCallback
import org.jahia.services.content.JCRSessionWrapper
import org.jahia.services.content.JCRTemplate

import javax.jcr.RepositoryException

final String workspace = Constants.EDIT_WORKSPACE
final Locale locale = null

JCRTemplate.getInstance().doExecuteWithSystemSessionAsUser(null, workspace, locale, new JCRCallback<Object>() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

        log.info("Running on the workspace " + session.getWorkspace().getName())

        return null
    }
})