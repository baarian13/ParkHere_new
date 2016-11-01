import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler
import braintree

braintree.Configuration.configure(braintree.Environment.Sandbox,
                                  merchant_id="5nhsztmvzrz5gyk8",
                                  public_key="mfhyjtgncvhnr76c",
                                  private_key="dacab9f3f1c7352878ce5bc475b5a082")


class GetClientTokenHandler(AbstractSpotHandler):
    '''
    -Requests are posted here when a user is creating an account.
    -All arguments are submitted as strings.
    '''
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        result = braintree.ClientToken.generate()
        self.write(result)
