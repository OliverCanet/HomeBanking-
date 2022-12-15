const { createApp } = Vue

let app = createApp({
    data() {
        return{
            
            url : "http://localhost:8080/api/clients/current",
            clientes : [],
            accounts: [],
            accountsExcludingO: [],
            radioTransfer:null,
            selectAccount:null,
            selectAccountTransferTo: '',
            amount: null,
            description: '',
            accountThirdD: '',



        }
    },
    mounted(){

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

        sendTransfer(){
            console.log(this.amount);
            console.log(this.description);
            console.log(this.selectAccount);
            console.log(this.accountThirdD);
            axios.post('/api/transactions',`amount=${this.amount}&description=${this.description}&accountO=${this.selectAccount}&accountD=${this.accountThirdD}`).then((response) =>  Swal.fire({
                icon: 'success',
                
                text: 'The transaction was a success',
               
              })).catch((error) => Swal.fire({
                icon: 'error',
                
                text: 'Something went wrong!',
                
              }))
           
        },

        surePopUp(){
            Swal.fire({
                title: 'Are you sure you want to make this transaction?',
                text: "You won't be able to revert this!",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes'
              }).then((result) => {
                if (result.isConfirmed) {
                    this.sendTransfer()
                }
              })
        }
      

    },
    computed:{
        selectedAccount(){
            this.accountsExcludingO = this.accounts.filter(account => account.number != this.selectAccount)
            
        }

    },
    
}).mount('#app');