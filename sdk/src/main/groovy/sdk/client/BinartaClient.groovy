package sdk.client

interface BinartaClient {
    def requiresAuthentication(args)
    def isAuthenticationRequired( args)
    def requiresPermission(args)
    def getRequiredPermissions(args)
}