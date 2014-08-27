package sdkx.rest.client.operations

class ConfigureGatewayOperation {
    @Delegate RestOperation delegate = new RestOperation()

    def execute(args) {
        delegate.execute(method: 'post', args: [
            path: 'config',
            body: [
                id   : "entity.gateway.$args.entity" as String,
                value: args.args
            ]
        ])
    }
}
