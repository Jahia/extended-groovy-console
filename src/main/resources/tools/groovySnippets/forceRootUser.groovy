import org.jahia.services.content.JCRSessionFactory
import org.jahia.services.content.decorator.JCRUserNode
import org.jahia.services.usermanager.JahiaUser
import org.jahia.services.usermanager.JahiaUserManagerService

JahiaUser realUser = JCRSessionFactory.getInstance().getCurrentUser()
if (!realUser.isRoot()) {
    JCRUserNode rootUser = JahiaUserManagerService.getInstance().lookupRootUser()
    JCRSessionFactory.getInstance().setCurrentUser(rootUser.getJahiaUser())
}

// write your code here

if (!realUser.isRoot()) {
    JCRSessionFactory.getInstance().setCurrentUser(realUser)
}