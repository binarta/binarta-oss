package sdkx.rest.client.operations

class RequiresAuthenticationOperation {
    @Delegate RestOperation delegate = new RestOperation()

    def execute(args) {
        delegate.execute(method: 'post', args: [
            path: '/config',
            body: [
                id   : "requires.authentication.${args.entity}.${args.action}".toString(),
                value: true
            ]
        ])
    }
}
