import org.jahia.services.content.JCRObservationManager

try {
    JCRObservationManager.setAllEventListenersDisabled(true)

    // write your code here

} finally {
    JCRObservationManager.setAllEventListenersDisabled(false)
}