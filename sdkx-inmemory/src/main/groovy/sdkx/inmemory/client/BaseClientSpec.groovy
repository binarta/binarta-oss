package sdkx.inmemory.client

abstract class BaseClientSpec extends ClientSpec {
    def "add entity"() {
        given:
        def source = [
            entity : 'permission',
            payload: [owner: 'U', name: 'P']
        ]

        when:
        client.addEntity(source)

        then:
        assertEntityAdded(source)
    }

    abstract void assertEntityAdded(payload)
}
