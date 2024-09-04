#!/bin/bash

USER_SERVICE_URL="http://localhost:8081"
ACCOUNT_SERVICE_URL="http://localhost:8082"
PAYMENT_GATEWAY_SERVICE_URL="http://localhost:8085"
TRANSACTION_SERVICE_URL="http://localhost:8084"
BANK_RECONCILIATION_SERVICE_URL="http://localhost:8083"
NOTIFICATION_SERVICE_URL="http://localhost:8086"

USER_EMAIL="loroye5769@alientex.com"
ADMIN_EMAIL="admin@example.com"
SAMPLE_BASE64_IMAGE="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQBKci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tCa00YFsffekFY+nUzFtjW0LrvjRXrCDIAaPLlW0nHL0SsZtVoaF98mLrx3pdhOqLtYPHChahZcYYO7KvPFxvRl5XPp1sN3adWiD1ZAqD6XYK1b/dvE5IWryTt2udLFedwc1+9kLp+vbbpoDh+6TklxBeAi9TL0taeWpdmZzQDry0AcO+jQ12RyohqqoYoo8RDwJrU+qXkjWtfi8Xxt58BdQuwQs9qC/afLwCw8tnQbqYAPsgxE1S6F3EAIXux2oQFKm0ihMsOF71dHYx+f3NND68ghCu1YIoePPQN1pGRABkJ6Bus96CutRZMydTl+TvuiRW1m3n0eDl0vRPcEysqdXn+jsQPsrHMquGeXEaY4Yk4wxWcY5V/9scqOMOVUFthatyTy8QyqwZ+kDURKoMWxNKr2EeqVKcTNOajqKoBgOE28U4tdQl5p5bwCw7BWquaZSzAPlwjlithJtp3pTImSqQRrb2Z8PHGigD4RZuNX6JYj6wj7O4TFLbCO/Mn/m8R+h6rYSUb3ekokRY6f/YukArN979jcW+V/S8g0eT/N3VN3kTqWbQ428m9/8k0P/1aIhF36PccEl6EhOcAUCrXKZXXWS3XKd2vc/TRBG9O5ELC17MmWubD2nKhUKZa26Ba2+D3P+4/MNCFwg59oWVeYhkzgN/JDR8deKBoD7Y+ljEjGZ0sosXVTvbc6RHirr2reNy1OXd6pJsQ+gqjk8VWFYmHrwBzW/n+uMPFiRwHB2I7ih8ciHFxIkd/3Omk5tCDV1t+2nNu5sxxpDFNx+huNhVT3/zMDz8usXC3ddaHBj1GHj/As08fwTS7Kt1HBTmyN29vdwAw+/wbwLVOJ3uAD1wi/dUH7Qei66PfyuRj4Ik9is+hglfbkbfR3cnZm7chlUWLdwmprtCohX4HUtlOcQjLYCu+fzGJH2QRKvP3UNz8bWk1qMxjGTOMThZ3kvgLI5AzFfo379UAAAAASUVORK5CYII="

# Define color codes for formatting
BLACK='\033[0;30m'
DARK_GRAY='\033[1;30m'
LIGHT_RED='\033[1;31m'
LIGHT_GREEN='\033[1;32m'
LIGHT_YELLOW='\033[1;33m'
LIGHT_BLUE='\033[1;34m'
PURPLE='\033[0;35m'
LIGHT_PURPLE='\033[1;35m'
CYAN='\033[0;36m'
LIGHT_CYAN='\033[1;36m'
LIGHT_GRAY='\033[0;37m'
WHITE='\033[1;37m'
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print headers
function print_header() {

    echo -e "                                      ${BLUE}============================================================================${NC}"
    echo -e "                                      ${NC}$1${NC}"
    echo -e "                                      ${BLUE}============================================================================${NC}"
}

# Function to print info messages
function print_info() {
    echo -e "         ${YELLOW}$1${NC}"
}

function print_header_2() {
    echo -e "\n"
    echo -e "                                      ${NC}==============================================================================${NC}"
    echo -e "                                                           ${NC}$1${NC}"
    echo -e "                                      ${NC}==============================================================================${NC}"
}

# Function to print success messages
function print_success() {
    echo -e "         ${GREEN}$1${NC}"
}

# Function to print error messages
function print_error() {
    echo -e "\n         ${RED}$1${NC}"
}

# Function to prompt user to continue
function pause() {
    # echo -e "${GREEN}Press Enter to continue...${NC}"
    read -p ""
}

# Function to check the response status
function check_response() {
    if [ $1 -ne 0 ]; then
        print_error "Error: Expected status 0, got $1"
        exit 1
    fi

    SUCCESS=$(echo $2 | jq -r '.success')
    MESSAGE=$(echo $2 | jq -r '.message')
    ERROR=$(echo $2 | jq -r '.data')

    if [ "$SUCCESS" != "true" ]; then
        print_error "Error: $MESSAGE"
        print_error "Data: $ERROR"
        exit 1
    fi

    print_success "Success: $MESSAGE"
}

clear

ascii_art="${LIGHT_RED}
                        ░██████╗░██╗░░░██╗░█████╗░███╗░░██╗████████╗██╗░░░██╗███╗░░░███╗  ░█████╗░░█████╗░░██████╗██╗░░██╗
                        ██╔═══██╗██║░░░██║██╔══██╗████╗░██║╚══██╔══╝██║░░░██║████╗░████║  ██╔══██╗██╔══██╗██╔════╝██║░░██║
                        ██║██╗██║██║░░░██║███████║██╔██╗██║░░░██║░░░██║░░░██║██╔████╔██║  ██║░░╚═╝███████║╚█████╗░███████║
                        ╚██████╔╝██║░░░██║██╔══██║██║╚████║░░░██║░░░██║░░░██║██║╚██╔╝██║  ██║░░██╗██╔══██║░╚═══██╗██╔══██║
                        ░╚═██╔═╝░╚██████╔╝██║░░██║██║░╚███║░░░██║░░░╚██████╔╝██║░╚═╝░██║  ╚█████╔╝██║░░██║██████╔╝██║░░██║
                        ░░░╚═╝░░░░╚═════╝░╚═╝░░╚═╝╚═╝░░╚══╝░░░╚═╝░░░░╚═════╝░╚═╝░░░░░╚═╝  ░╚════╝░╚═╝░░╚═╝╚═════╝░╚═╝░░╚═╝

${LIGHT_YELLOW}
            █▀█ █▀█ █▀▀ █▀ █▀▀ █▄░█ ▀█▀ █▀▀ █▀▄   █▄▄ █▄█   ▀█▀ █░█ █▀▀   █▀█ █░█ ▄▀█ █▄░█ ▀█▀ █░█ █▀▄▀█ █▀▄ █▀█ ▄▀█ █▀▀ █▀█ █▄░█ █▀
            █▀▀ █▀▄ ██▄ ▄█ ██▄ █░▀█ ░█░ ██▄ █▄▀   █▄█ ░█░   ░█░ █▀█ ██▄   ▀▀█ █▄█ █▀█ █░▀█ ░█░ █▄█ █░▀░█ █▄▀ █▀▄ █▀█ █▄█ █▄█ █░▀█ ▄█
${NC}
"

echo -e "$ascii_art"

print_header "Comprehensive Quantum Cash Test Script: A Real-World User Journey"

# User Registration and KYC Process
print_header_2 "1. User Registration"
pause

# Define user information variables
PASSWORD_HASH="password"
FIRST_NAME="Arjay"
LAST_NAME="Warjay"
DATE_OF_BIRTH="1990-01-01"
PHONE_NUMBER="+1234567890"
ADDRESS_LINE1="123 Arjay St"
ADDRESS_LINE2="Apartment 1Arjay"
CITY="Arjaytown"
STATE="Arjaystate"
COUNTRY="Arjaycountry"
POSTAL_CODE="12345"

# Display user information
echo -e "${YELLOW}  User Details:${NC}"
print_info "Email: $USER_EMAIL"
print_info "Password: $PASSWORD_HASH"
print_info "First Name: $FIRST_NAME"
print_info "Last Name: $LAST_NAME"
print_info "Date of Birth: $DATE_OF_BIRTH"
print_info "Phone Number: $PHONE_NUMBER"
print_info "Address Line 1: $ADDRESS_LINE1"
print_info "Address Line 2: $ADDRESS_LINE2"
print_info "City: $CITY"
print_info "State: $STATE"
print_info "Country: $COUNTRY"
print_info "Postal Code: $POSTAL_CODE"

pause

REGISTER_RESPONSE=$(http POST "${USER_SERVICE_URL}/api/users" \
    email="${USER_EMAIL}" \
    passwordHash="${PASSWORD_HASH}" \
    firstName="${FIRST_NAME}" \
    lastName="${LAST_NAME}" \
    dateOfBirth="${DATE_OF_BIRTH}" \
    phoneNumber="${PHONE_NUMBER}" \
    addressLine1="${ADDRESS_LINE1}" \
    addressLine2="${ADDRESS_LINE2}" \
    city="${CITY}" \
    state="${STATE}" \
    country="${COUNTRY}" \
    postalCode="${POSTAL_CODE} ")

check_response $? "$REGISTER_RESPONSE"
USER_ID=$(echo $REGISTER_RESPONSE | jq -r '.data.id')
print_success "User registered with ID: $USER_ID"
echo -e "$REGISTER_RESPONSE" | jq .

pause

print_header_2 "2. User Login"
print_info "User Email: $USER_EMAIL"
print_info "User Password: $PASSWORD_HASH"

pause

LOGIN_RESPONSE=$(http POST "${USER_SERVICE_URL}/api/users/login" \
    email="${USER_EMAIL}" \
    password="${PASSWORD_HASH}")

check_response $? "$LOGIN_RESPONSE"
TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.data.token')
print_success "Login successful, token: $TOKEN"

echo -e "$LOGIN_RESPONSE" | jq .

pause

echo -e "\n"
print_header_2 "3. Initiate KYC Process"

pause

KYC_INIT_RESPONSE=$(http POST "${USER_SERVICE_URL}/api/users/${USER_ID}/kyc" \
    "Authorization: Bearer $TOKEN")

check_response $? "$KYC_INIT_RESPONSE"
KYC_ID=$(echo $KYC_INIT_RESPONSE | jq -r '.data')
print_success "KYC process initiated with ID: $KYC_ID"

echo -e "$KYC_INIT_RESPONSE" | jq .

echo -e "\n"
print_header_2 "4. Submit KYC Information"

pause

# Define KYC information variables
ID_TYPE="Passport"
ID_NUMBER="A1234567"
ID_EXPIRY_DATE="2025-12-31T23:59:59Z"
ADDRESS_PROOF_TYPE="Utility Bill"

print_info "Id Type: $ID_TYPE"
print_info "Id Number: $ID_NUMBER"
print_info "Id Expiry Date: $ID_EXPIRY_DATE"
print_info "Address Proof Type: $ADDRESS_PROOF_TYPE"
print_info "Address Proof Document: $SAMPLE_BASE64_IMAGE"

pause

# Submit KYC information
KYC_SUBMIT_RESPONSE=$(http POST "${USER_SERVICE_URL}/api/users/${KYC_ID}/kyc/submit" \
    "Authorization: Bearer $TOKEN" \
    idType="${ID_TYPE}" \
    idNumber="${ID_NUMBER}" \
    idExpiryDate="${ID_EXPIRY_DATE}" \
    addressProofType="${ADDRESS_PROOF_TYPE}" \
    addressProofDocument="${SAMPLE_BASE64_IMAGE}" \
    idProofDocument="${SAMPLE_BASE64_IMAGE}")
check_response $? "$KYC_SUBMIT_RESPONSE"
print_success "KYC information submitted successfully"

echo -e "\n"
echo -e "$KYC_SUBMIT_RESPONSE" | jq .

pause

print_header_2 "5. Admin Login"
print_info "Admin Email: $ADMIN_EMAIL"
print_info "Admin Password: $PASSWORD_HASH"

pause

LOGIN_RESPONSE=$(http POST "${USER_SERVICE_URL}/api/users/login" \
    email="${ADMIN_EMAIL}" \
    password="${PASSWORD_HASH}")

check_response $? "$LOGIN_RESPONSE"
ADMIN_TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.data.token')
print_success "Admin Login successful, token: $ADMIN_TOKEN"

pause

# Simulate KYC review and approval (admin action)
print_header_2 "6. KYC Approval - Admin Action"

pause

KYC_APPROVE_RESPONSE=$(http PUT "${USER_SERVICE_URL}/api/users/${KYC_ID}/kyc/approve" \
    "Authorization: Bearer $ADMIN_TOKEN")
check_response $? "$KYC_APPROVE_RESPONSE"
STRIPE_CUSTOMER_ID=$(echo $KYC_APPROVE_RESPONSE | jq -r '.data.stripeCustomerId')
print_success "KYC approved successfully, created Stripe Customer ID: $STRIPE_CUSTOMER_ID"

echo -e "\n\n"
echo -e "$KYC_APPROVE_RESPONSE" | jq .

pause

# Account Creation
print_header_2 "7. Create Savings Account"

pause

# Define account creation variables
ACCOUNT_TYPE="SAVINGS"
CURRENCY="USD"

print_info "Account Type: $ACCOUNT_TYPE"
print_info "Currency: $CURRENCY"

pause

ACCOUNT_RESPONSE=$(http POST "${ACCOUNT_SERVICE_URL}/api/accounts" \
    "Authorization: Bearer $TOKEN" \
    accountType="${ACCOUNT_TYPE}" \
    currency="${CURRENCY}")

check_response $? "$ACCOUNT_RESPONSE"
ACCOUNT_ID=$(echo $ACCOUNT_RESPONSE | jq -r '.data.id')
print_success "Savings account created with ID: $ACCOUNT_ID"

echo -e "\n\n"
echo -e "$ACCOUNT_RESPONSE" | jq .

pause

# Account Creation 2
print_header_2 "8. Create Checking Account"

pause

# Define account creation variables
ACCOUNT_TYPE="CHECKING"
CURRENCY="USD"

print_info "Account Type: $ACCOUNT_TYPE"
print_info "Currency: $CURRENCY"

pause

ACCOUNT_RESPONSE=$(http POST "${ACCOUNT_SERVICE_URL}/api/accounts" \
    "Authorization: Bearer $TOKEN" \
    accountType="${ACCOUNT_TYPE}" \
    currency="${CURRENCY}")

check_response $? "$ACCOUNT_RESPONSE"
ACCOUNT_ID_2=$(echo $ACCOUNT_RESPONSE | jq -r '.data.id')
print_success "Checking account created with ID: $ACCOUNT_ID_2"

echo -e "\n\n"
echo -e "$ACCOUNT_RESPONSE" | jq .

# Add Payment Method (Stripe)
print_header_2 "9. Add Payment Method"

pause

METHOD_TYPE="CREDIT_CARD"
PROVIDER="STRIPE"
TOKEN_ID="tok_visa"
LAST_4="4242"

print_info "Payment Method Type: $METHOD_TYPE"
print_info "Provider: $PROVIDER"
print_info "Token ID: $TOKEN_ID"
print_info "Last 4: $LAST_4"

pause

PAYMENT_METHOD_RESPONSE=$(http POST "${USER_SERVICE_URL}/api/users/${USER_ID}/payment-method" \
    "Authorization: Bearer $TOKEN" \
    methodType="${METHOD_TYPE}" \
    provider="${PROVIDER}" \
    tokenId="${TOKEN_ID}" \
    last4="${LAST_4}")

check_response $? "$PAYMENT_METHOD_RESPONSE"
PAYMENT_METHOD_ID=$(echo $PAYMENT_METHOD_RESPONSE | jq -r '.data.id')
print_success "Stripe payment method added with ID: $PAYMENT_METHOD_ID"

echo -e "\n\n"
echo -e "$PAYMENT_METHOD_RESPONSE" | jq .

pause

# Deposit Funds
print_header_2 "10. Perform a Deposit Transaction"

pause
AMOUNT=500000.00
CURRENCY="USD"
DESCRIPTION="Deposit into account"
TRANSACTION_TYPE="DEPOSIT"
PAYMENT_METHOD="pm_card_visa"

print_info "Destination Account ID: $ACCOUNT_ID_2"
print_info "Amount: $AMOUNT"
print_info "Currency: $CURRENCY"
print_info "Description: $DESCRIPTION"
print_info "Transaction Type: $TRANSACTION_TYPE"
print_info "Payment Method: $PAYMENT_METHOD"
print_info "Customer ID: $CUSTOMER_ID"

pause

DEPOSIT_RESPONSE=$(http POST "${TRANSACTION_SERVICE_URL}/api/transactions/deposit" \
    "Authorization: Bearer $TOKEN" \
    destinationAccountId="${ACCOUNT_ID}" \
    amount:=${AMOUNT} \
    currency="${CURRENCY}" \
    description="${DESCRIPTION}" \
    transactionType="${TRANSACTION_TYPE}" \
    paymentMethod="${PAYMENT_METHOD}" \
    customerId="${STRIPE_CUSTOMER_ID}")

check_response $? "$DEPOSIT_RESPONSE"
TRANSACTION_ID=$(echo $DEPOSIT_RESPONSE | jq -r '.data.id')
print_success "Deposit transaction completed with ID: $TRANSACTION_ID"

echo -e "\n\n"
echo -e "$DEPOSIT_RESPONSE" | jq .

pause

# Check Account Balance
print_header_2 "11. Check Account Balance"

pause

BALANCE_RESPONSE=$(http GET "${ACCOUNT_SERVICE_URL}/api/accounts/${ACCOUNT_ID}/balance" \
    "Authorization: Bearer $TOKEN")
CURRENT_BALANCE=$(echo $BALANCE_RESPONSE | jq -r '.data.balance')
check_response $? "$BALANCE_RESPONSE"
print_success "Current account balance: $CURRENT_BALANCE USD"
echo -e "\n\n"
echo -e "$BALANCE_RESPONSE" | jq .

pause

# Withdraw funds
print_header_2 "12. Perform a Withdrawal Transaction"

pause

AMOUNT=20000.00
CURRENCY="USD"
DESCRIPTION="Withdraw from account"
TRANSACTION_TYPE="WITHDRAW"
PAYMENT_METHOD="pm_card_visa"

print_info "Destination Account ID: $ACCOUNT_ID"
print_info "Amount: $AMOUNT"
print_info "Currency: $CURRENCY"
print_info "Description: $DESCRIPTION"
print_info "Transaction Type: $TRANSACTION_TYPE"
print_info "Payment Method: $PAYMENT_METHOD"
print_info "Customer ID: $STRIPE_CUSTOMER_ID"

pause

WITHDRAWAL_RESPONSE=$(http POST "${TRANSACTION_SERVICE_URL}/api/transactions/withdraw" \
    "Authorization: Bearer $TOKEN" \
    destinationAccountId="${ACCOUNT_ID}" \
    amount:=${AMOUNT} \
    currency="${CURRENCY}" \
    description="${DESCRIPTION}" \
    transactionType="${TRANSACTION_TYPE}" \
    paymentMethod="${PAYMENT_METHOD}" \
    customerId="${STRIPE_CUSTOMER_ID}")

check_response $? "$WITHDRAWAL_RESPONSE"
TRANSACTION_ID=$(echo $WITHDRAWAL_RESPONSE | jq -r '.data.id')
print_success "Withdrawal transaction completed with ID: $TRANSACTION_ID"

echo -e "\n\n"
echo -e "$WITHDRAWAL_RESPONSE" | jq .

pause

# Check Updated Balance
print_header_2 "13. Check Updated Balance"

pause

UPDATED_BALANCE_RESPONSE=$(http GET "${ACCOUNT_SERVICE_URL}/api/accounts/${ACCOUNT_ID}/balance" \
    "Authorization: Bearer $TOKEN")

check_response $? "$UPDATED_BALANCE_RESPONSE"
UPDATED_BALANCE=$(echo $UPDATED_BALANCE_RESPONSE | jq -r '.data.balance')
print_info "Updated account balance: $UPDATED_BALANCE USD"
echo -e "\n\n"
echo -e "$UPDATED_BALANCE_RESPONSE" | jq .

pause

print_header_2 "14. Perform a Transfer Transaction"

pause

# Perform Internal Transfer
AMOUNT=50.00
CURRENCY="USD"
DESCRIPTION="Transfer between accounts"
TRANSACTION_TYPE="TRANSFER"
PAYMENT_METHOD="pm_card_visa"

print_info "Destination Account ID: $ACCOUNT_ID"
print_info "Source Account ID: $ACCOUNT_ID_2"
print_info "Amount: $AMOUNT"
print_info "Currency: $CURRENCY"
print_info "Description: $DESCRIPTION"
print_info "Transaction Type: $TRANSACTION_TYPE"
print_info "Payment Method: $PAYMENT_METHOD"
print_info "Customer ID: $STRIPE_CUSTOMER_ID"

pause

TRANSFER_RESPONSE=$(http POST "${TRANSACTION_SERVICE_URL}/api/transactions/transfer" \
    "Authorization: Bearer $TOKEN" \
    destinationAccountId="${ACCOUNT_ID_2}" \
    sourceAccountId="${ACCOUNT_ID}" \
    amount:=${AMOUNT} \
    currency="${CURRENCY}" \
    description="${DESCRIPTION}" \
    transactionType="${TRANSACTION_TYPE}" \
    paymentMethod="${PAYMENT_METHOD}" \
    customerId="${STRIPE_CUSTOMER_ID}")

check_response $? "$TRANSFER_RESPONSE"
echo "Transfer transaction completed"
echo -e "$TRANSFER_RESPONSE" | jq .

pause

# Check Updated Balance
print_header_2 "15. Check Updated Balance"

pause

UPDATED_BALANCE_RESPONSE=$(http GET "${ACCOUNT_SERVICE_URL}/api/accounts/${ACCOUNT_ID}/balance" \
    "Authorization: Bearer $TOKEN")

check_response $? "$UPDATED_BALANCE_RESPONSE"
UPDATED_BALANCE=$(echo $UPDATED_BALANCE_RESPONSE | jq -r '.data.balance')
print_success "Updated account balance: $UPDATED_BALANCE USD"

echo -e "$UPDATED_BALANCE_RESPONSE" | jq .

pause

# Simulate End-of-Day Reconciliation Process
print_header_2 "16. Initiate End-of-Day Reconciliation - Admin Action"

pause

RECONCILIATION_INIT_RESPONSE=$(http GET "${BANK_RECONCILIATION_SERVICE_URL}/api/reconciliation/manual-trigger" \
    "Authorization: Bearer $ADMIN_TOKEN")
check_response $? "$RECONCILIATION_INIT_RESPONSE"
print_success "Reconciliation initiated successfully"
echo -e "$RECONCILIATION_INIT_RESPONSE" | jq .

pause

# Fetch Stripe Transactions for Reconciliation
print_header_2 "17. Fetch Stripe Transactions for Reconciliation"

pause

STRIPE_TRANSACTIONS_RESPONSE=$(http GET "${BANK_RECONCILIATION_SERVICE_URL}/api/reconciliation/fetchPaymentIntents/startDate=2024-07-25T12:00:00/endDate=2024-08-31T23:59:59" \
    "Authorization: Bearer $ADMIN_TOKEN")
check_response $? "$STRIPE_TRANSACTIONS_RESPONSE"
print_success "Stripe transactions fetched successfully"
echo -e "$STRIPE_TRANSACTIONS_RESPONSE" | jq .

pause

# Fetch Internal Transactions for Reconciliation
print_header_2 "18. Fetch Internal Transactions for Reconciliation"

pause

INTERNAL_TRANSACTIONS_RESPONSE=$(http GET "${BANK_RECONCILIATION_SERVICE_URL}/api/reconciliation/fetchInternalTransactions/startDate=2024-07-25T12:00:00/endDate=2024-08-31T23:59:59" \
    "Authorization: Bearer $ADMIN_TOKEN")
check_response $? "$INTERNAL_TRANSACTIONS_RESPONSE"
print_success "Internal transactions fetched successfully"
echo -e "$INTERNAL_TRANSACTIONS_RESPONSE" | jq .

pause

# Perform Reconciliation
print_header_2 "19. Perform Reconciliation Matching, Generate Reconciliation Report and Complete Reconilication"
REPORT_RESPONSE=$(http GET "${BANK_RECONCILIATION_SERVICE_URL}/api/reconciliation/generateReport/startDate=2024-07-25T12:00:00/endDate=2024-08-31T23:59:59/userId=${USER_ID}" \
    "Authorization: Bearer $ADMIN_TOKEN")
MATCHED_COUNT=$(echo $REPORT_RESPONSE | jq -r '.data.summary.matchedTransactions')
UNMATCHED_COUNT=$(echo $REPORT_RESPONSE | jq -r '.data.summary.unmatchedTransactions')
RECONCILLIATION_STATUS=$(echo $REPORT_RESPONSE | jq -r '.data.reconciliationStatus')
print_success "Reconciliation matched $MATCHED_COUNT transactions, unmatched $UNMATCHED_COUNT transactions"

pause

check_response $? "$REPORT_RESPONSE"
print_success "Reconciliation report generated and status set to $RECONCILLIATION_STATUS"

pause

echo -e "$REPORT_RESPONSE" | jq .data

pause

# Simulating token revocation (logout)
print_header_2 "20. User Logout"

pause
# Ensure no extra spaces or newline characters in TOKEN
TOKEN=$(echo -n $TOKEN)

LOGOUT_RESPONSE=$(http GET "${USER_SERVICE_URL}/api/users/logout" \
    "Authorization: Bearer $TOKEN")

check_response $? "$LOGOUT_RESPONSE"
print_success "User logged out successfully"
echo -e "$LOGOUT_RESPONSE" | jq .

pause

# Simulating admin token revocation (logout)
print_header_2 "21. Admin Logout"

pause

# Ensure no extra spaces or newline characters in ADMIN_TOKEN
ADMIN_TOKEN=$(echo -n $ADMIN_TOKEN)

LOGOUT_RESPONSE=$(http GET "${USER_SERVICE_URL}/api/users/logout" \
    "Authorization: Bearer $ADMIN_TOKEN")

check_response $? "$LOGOUT_RESPONSE"
print_success "Admin logged out successfully"
echo -e "$LOGOUT_RESPONSE" | jq .

pause

print_header "Comprehensive test script completed successfully."

ascii_art="${LIGHT_RED}
                        
                                        ████████╗██╗░░██╗░█████╗░███╗░░██╗██╗░░██╗  ██╗░░░██╗░█████╗░██╗░░░██╗██╗██╗██╗
                                        ╚══██╔══╝██║░░██║██╔══██╗████╗░██║██║░██╔╝  ╚██╗░██╔╝██╔══██╗██║░░░██║██║██║██║
                                        ░░░██║░░░███████║███████║██╔██╗██║█████═╝░  ░╚████╔╝░██║░░██║██║░░░██║██║██║██║
                                        ░░░██║░░░██╔══██║██╔══██║██║╚████║██╔═██╗░  ░░╚██╔╝░░██║░░██║██║░░░██║╚═╝╚═╝╚═╝
                                        ░░░██║░░░██║░░██║██║░░██║██║░╚███║██║░╚██╗  ░░░██║░░░╚█████╔╝╚██████╔╝██╗██╗██╗
                                        ░░░╚═╝░░░╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░░╚══╝╚═╝░░╚═╝  ░░░╚═╝░░░░╚════╝░░╚═════╝░╚═╝╚═╝╚═╝

${NC}
"

echo -e "$ascii_art"
