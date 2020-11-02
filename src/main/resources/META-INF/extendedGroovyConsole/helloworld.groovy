if (active) {
    log.info(String.format("Hello %s!!!", name == null || name.trim().length() == 0 ? "world" : name))
} else {
    log.info("On mute")
}