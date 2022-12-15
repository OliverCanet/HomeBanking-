const { createApp } = Vue


let app = createApp({
    data() {
        return{
            url : "http://localhost:8080/api/clients/current",
            clientes : [],
            accounts : [],
            accountsActive: [],
            cardsActive: [],
            loans : [],
            cards : [],
            name: '',
            lastName: '',
            email:'',
            typeAccount: null,
            accountNumberD: null,
            cardNumberD: null,
            cardsDebit: [],
            cardsCredit:[],
            num : 0,
            emailLogIn: '',
            passwordLogIn: '',
            lastName2: '',
            firstName: '',
            emailSign: '',
            passwordSign: '',
            errorSignIn: false,
            errorRegister: false,
            errorCard:false,
            buttonAccount: false,
            radioType: null,
            radioColor: null,
            
            




        }
    },
    created(){
        this.loadData(this.url)


    },

    methods:{
        loadData(url) {

            axios.get(url)

            .then((data) => {
                this.clientes = data.data
                this.accounts = this.clientes.accounts
                this.loans = this.clientes.loans
                this.cards = this.clientes.cards
                this.accountsActive = this.accounts
                this.cardsActive = this.cards
                console.log(this.clientes);
                console.log(this.loans);
                this.filterAccountActive();
                this.filterCardsActive();
                this.filterCards();
                


            })
            .catch((error) => {
                console.log(error);
            })
        },
        signIn(){

            axios.post('/api/login',`email=${this.emailLogIn}&password=${this.passwordLogIn}`).then(response => window.location.href = "http://localhost:8080/web/accounts.html").catch((error)=>  console.log(error))


        },
        logOut(){
            return axios.post('/api/logout').then(response=> window.location.href = "http://localhost:8080/web/index.html")
        },
        register(){
            
            axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName2}&email=${this.emailLogIn}&password=${this.passwordLogIn}`).then(response => this.signIn()).catch((error)=> this.errorRegister = true)
        },
        addAccount(){
            axios.post('/api/clients/current/accounts',`type=${this.typeAccount}`).then(response => window.location.href = "http://localhost:8080/web/accounts.html").catch((error)=> this.buttonAccount = true)
        },
        deleteAccount(){
            axios.patch('/api/clients/current/accounts/delete',`number=${this.accountNumberD}`).then(response => window.location.href = "http://localhost:8080/web/accounts.html");
        },
        createCard(){
            axios.post('/api/clients/current/cards',`type=${this.radioType}&color=${this.radioColor}`).then(response => window.location.href = "http://localhost:8080/web/cards.html").catch((error)=>  this.errorCard = true)

        },
        deleteCard(){
            axios.patch('/api/clients/current/cards/delete',`number=${this.cardNumberD}`).then(response => window.location.href = "http://localhost:8080/web/cards.html").catch((error)=>  this.errorCard = true)
        },
        
        agregarCliente(){
            let client = {
                firstName: this.name,
                lastName: this.lastName,
                email : this.email,

            }
            this.postClient(client)


        },
        postClient(cliente){
            axios.post(this.url, cliente)
            .then(this.loadData(this.url));

        },
        eliminarCliente(cliente){
            axios.delete(cliente._links.self.href)
            .then(this.loadData(this.url));



        },
        filterCards(){
            this.cardsCredit = this.cardsActive.filter(card => card.type === "CREDIT")
            this.cardsDebit = this.cardsActive.filter(card => card.type === "DEBIT") 
        },
        filterAccountActive(){
            this.accountsActive = this.accounts.filter(account => account.active === true)
        },
        filterCardsActive(){
            this.cardsActive = this.cards.filter(card => card.active === true)
        }


        
        

    },
    computed:{
        

    },

})

app.mount('#app')