package sdkx.rest.client.operations

class AddEntityOperation {
    @Delegate RestOperation delegate = new RestOperation()

    def execute(args) {
        delegate.execute(method: 'put', args: [
            path: "api/entity/$args.entity",
            body: args.payload
        ])
    }
}
