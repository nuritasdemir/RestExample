# RestExample
Basic java rest api example with jersey and jetty.

Api has been tested with JUnit. In order to test the lock usage with parallel requests following commands can be used on a suitable enviorenment like linux or mac with homebrew.

    openaccount() {
        curl -s -XPOST "http://localhost:8080/bank-account/open?userId=1"  > /dev/null
    }
    export -f openaccount

    seq 1000 | parallel -j60 openaccount

After this, next created account should have ID 1001

    transfermoney() {
      curl -s -XPOST "http://localhost:8080/bank-account/deposit?account-id=1&amount=2"  > /dev/null &&
        curl -s -XPOST "http://localhost:8080/money-transfer/transfer?source-account-id=1&destination-account-id=2&amount=1"  > /dev/null   
    }
    export -f transfermoney

    seq 1000 | parallel -j60 transfermoney
    
After this, both account shoudl have exacly 1000 in balance.    
