if (System.getProperty("interruptScript") != null) {
    log.info("Script interrupted")
    System.clearProperty("interruptScript")
    break
}