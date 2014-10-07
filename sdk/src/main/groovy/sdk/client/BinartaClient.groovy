package sdk.client

import groovy.transform.CompileStatic

interface BinartaClient {
    def requiresAuthentication(args)
    def isAuthenticationRequired( args)
    def requiresPermission(args)
    def getRequiredPermission(args)
    def configureGateway(args)
    def addEntity(args)

    @CompileStatic
    class Unauthorized extends RuntimeException {}

    @CompileStatic
    class Forbidden extends RuntimeException {}

    @CompileStatic
    class NotFound extends RuntimeException {}

    @CompileStatic
    static class Error extends RuntimeException {
        Error(message) {super(message as String) }
    }
}