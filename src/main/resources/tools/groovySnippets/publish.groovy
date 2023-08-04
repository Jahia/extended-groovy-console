import org.jahia.api.Constants
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.services.content.JCRPublicationService
import org.jahia.services.content.decorator.JCRSiteNode

private void publishNode(JCRNodeWrapper node) {
    final JCRSiteNode site = node.getResolveSite()
    if (site == null) publishNode(node, null)

    publishNode(node, site.getActiveLiveLanguages())
}

private void publishNode(JCRNodeWrapper node, Set<String> languages) {
    publishNode(node, languages, false)
}

private void publishTree(JCRNodeWrapper node) {
    final JCRSiteNode site = node.getResolveSite()
    if (site == null) publishNode(node, null)

    publishNode(node, site.getActiveLiveLanguages(), true)
}

private void publishNode(JCRNodeWrapper node, Set<String> languages, boolean allSubTree) {
    JCRPublicationService.getInstance().publishByMainId(node.getIdentifier(),
            Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE, languages, allSubTree, null)
}

private void publishSingleNode(JCRNodeWrapper node) {
    JCRPublicationService.getInstance().publish(Collections.singleton(node.getIdentifier()).asList(),
            Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE, false, null)
}