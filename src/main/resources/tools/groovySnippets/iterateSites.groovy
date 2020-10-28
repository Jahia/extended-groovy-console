import org.jahia.services.content.decorator.JCRSiteNode
import org.jahia.services.sites.JahiaSitesService

for (JCRSiteNode site : JahiaSitesService.getInstance().getSitesNodeList(session)) {
    log.info("Processing site " + site.getTitle())
}