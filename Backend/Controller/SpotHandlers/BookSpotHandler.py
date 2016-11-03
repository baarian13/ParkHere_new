import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler
import braintree

braintree.Configuration.configure(braintree.Environment.Sandbox,
                                  merchant_id="5nhsztmvzrz5gyk8",
                                  public_key="mfhyjtgncvhnr76c",
                                  private_key="dacab9f3f1c7352878ce5bc475b5a082")

SUCCESS = '200'
FAILURE = '401'


class BookSpotHandler(AbstractSpotHandler):
    '''
    -Requests are posted here when a user is creating an account.
    -All arguments are submitted as strings.
    '''
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def post(self):
        result = SUCCESS
        try:
            payment_method_nonce = self.get_argument("payment_method_nonce","")
            price = self.get_argument("price","")
            result = braintree.Transaction.sale({
                "amount": price,
                "payment_method_nonce": payment_method_nonce,
                "options": {
                  "submit_for_settlement": True
                }
            })
            if result.is_success:
                renterEmail = self.get_argument("email", "")
                spotID = self.get_argument("spotID", "")
                self.db.bookSpot(renterEmail, spotID)
            else:
                result = FAILURE
        except:
            result = FAILURE

        self.write(result)
