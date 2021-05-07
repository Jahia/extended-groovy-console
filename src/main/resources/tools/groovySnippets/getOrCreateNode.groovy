import org.jahia.services.content.JCRNodeWrapper

private JCRNodeWrapper getOrCreate(JCRNodeWrapper parent, String name, String type) {
    return getOrCreate(parent, name, type, true)
}

private JCRNodeWrapper getOrCreate(JCRNodeWrapper parent, String name, String type, boolean checkTypeIfExists) {
    if (parent.hasNode(name)) {
        final JCRNodeWrapper node = parent.getNode(name)
        if (checkTypeIfExists) return node.isNodeType(type) ? node : null
        return node
    }
    return parent.addNode(name, type)
}