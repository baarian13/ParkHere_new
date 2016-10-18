'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado
from tornado import netutil, process, httpserver
from Controller.AbstractBaseHandler import MainHandler
from Controller.UserHandlers.SinginHandler import SigninHandler
from tornado.ioloop import IOLoop
settings = {
    "cookie_secret": "__TODO:_GENERATE_RANDOM_VALUE_HERE__",
    "login_url": "/signin",
}
application = tornado.web.Application([
    (r"/", MainHandler),
    (r"/signin", SigninHandler),
], **settings)

def main():
    sockets = netutil.bind_sockets(8888)
    process.fork_processes(0)
    server = httpserver.HTTPServer(application)
    server.add_sockets(sockets)
    IOLoop.current().start()

if __name__ == "__main__":
    main()