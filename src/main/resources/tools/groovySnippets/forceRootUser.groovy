import org.jahia.services.content.JCRSessionFactory
import org.jahia.services.content.decorator.JCRUserNode
import org.jahia.services.usermanager.JahiaUser
import org.jahia.services.usermanager.JahiaUserManagerService

JahiaUser realUser = JCRSessionFactory.getInstance().getCurrentUser()
boolean changeUser = realUser == null || !realUser.isRoot()
if (changeUser) {
    JCRUserNode rootUser = JahiaUserManagerService.getInstance().lookupRootUser()
    JCRSessionFactory.getInstance().setCurrentUser(rootUser.getJahiaUser())
}

try {

    // write your code here

} finally {
    if (changeUser) {
        JCRSessionFactory.getInstance().setCurrentUser(realUser)
    }
}