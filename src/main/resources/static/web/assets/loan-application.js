const { createApp } = Vue

let app = createApp({
    data() {
        return{
            
            url : "http://localhost:8080/api/clients/current",
            urlLoans : "http://localhost:8080/api/loans",
            clientes : [],
            accounts : [],
            loans: [],
            loanSelected: [],
            loanPayments: [],
            paymentSelected: [],
            selectLoanId: null,
            selectAccount: null,
            selectPayment: null,
            amountPorcentaje: null,
            eachPayment: 0,
            amount: '',



        }
    },
    mounted(){

        this.loadDataLoan()
        this.loadData()

    },

    methods:{
        loadData() {
            axios.get(this.url)

            .then((data) => {

                this.clientes = data.data
                
                this.accounts = this.clientes.accounts
                

                


            })
            .catch((error) => {

                console.log(error);

            })
        },
        loadDataLoan(){
            axios.get(this.urlLoans)

            .then((data) => {

                this.loans = data.data
                this.loanSelected = this.loans
                
                console.log(this.loanPayments);
                console.log(this.loans);
                

                


            })
            .catch((error) => {

                console.log(error);

            })
        },
        applyLoan(){
            this.paymentSelected.push(this.selectPayment);
            
            axios.post('/api/loans',{
                "idLoan": this.selectLoanId,
                "amount": this.amount,
                "payments": this.paymentSelected,
                "accountDestiny": this.selectAccount
            }).then(response => {

                
                
                Swal.fire({
                    icon: 'success',
                    
                    text: 'The transaction was a success', })

                    this.paymentSelected = [];
                
            } 
                ).catch((error)=> Swal.fire({
                icon: 'error',
                
                text: 'Something went wrong!',
                
              }))
        },
        surePopUp(){
            Swal.fire({
                title: 'Are you sure you want to apply for this loan?',
                text: `Each payment will be $${this.eachPayment}.`,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes'
              }).then((result) => {
                if (result.isConfirmed) {
                    this.applyLoan()
                }
              })
        }


        
    },
    computed:{

        selectLoan(){
            this.loanSelected = this.loans.filter(loan => loan.id == this.selectLoanId)
        },
        paymentSummary(){
            
            if(this.selectLoanId == 12)
            {
                this.amountPorcentaje = this.amount * 1.05
                
            }else if(this.selectLoanId == 13)
            {
                this.amountPorcentaje = this.amount * 1.1
                
            }else if(this.selectLoanId == 14)
            {
                this.amountPorcentaje = this.amount * 1.2

            }
            this.eachPayment = this.amountPorcentaje / this.selectPayment

        }
    },
    
}).mount('#app');