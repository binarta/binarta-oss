package client

interface RestClient {
    Response get(Map details)
    Response post(Map details)
    Response put(Map details)
    Response delete(Map details)

    void setBasicCredentials(String username, String password)

    interface Response {
        int getStatus()
        String getContentType()
        Object getData()
    }
}
