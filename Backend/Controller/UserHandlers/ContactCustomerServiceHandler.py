import tornado.web
import tornado.gen
from Controller.UserHandlers.AbstractUserHandler import AbstractUserHandler
import smtplib

SUCCESS = '200'
FAILURE = '401'
PARTIAL = '206'


class ContactCustomerServiceHandler(AbstractUserHandler):

    #@tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def post(self):
        result = SUCCESS
        email = self.get_argument("email")
        messagetext = self.get_argument("message")
        if email and messagetext:
            try:
            

                FROM = email
                TO = ["rsieh@usc.edu"] # must be a list

                SUBJECT = "Customer Service ParkHere " + email

                TEXT = messagetext

                # Prepare actual message

                message = """\
                From: %s
                To: %s
                Subject: %s

                %s
                """ % (FROM, ", ".join(TO), SUBJECT, TEXT)

                # Send the mail
                mail = smtplib.SMTP('smtp.gmail.com',587)

                mail.ehlo()

                mail.starttls()

                print 0
                mail.login('parkhere11b@gmail.com','dqIrS3zNNo')
                print 1
                mail.sendmail(email, TO, message)
                print 2
                mail.close()
            except:
                result = PARTIAL
        else:
            result = FAILURE

        return result
