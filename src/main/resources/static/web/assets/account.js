const { createApp } = Vue

let app = createApp({
    data() {
        return{
            param: new URLSearchParams(location.search).get("id"),
            url : "/api/accounts/",
            transactions : [],
            accounts : [],
            search: '',
            transactionsFiltrados: [],

        }
    },
    mounted(){

        this.loadData()

    },

    methods:{
        loadData() {
            axios.get(this.url + this.param)

            .then((data) => {
                this.accounts = data.data
                this.transactions = this.accounts.transactions
                this.transactionsFiltrados = this.accounts.transactions
                console.log(this.transactionsFiltrados);
                console.log(this.accounts);
                


            })
            .catch((error) => {

                console.log(error);

            })
        },

    },
    computed:{
        filtroSearch() {
            this.transactionsFiltrados = this.transactions.filter(transaction => transaction.description.toLowerCase().includes(this.search.toLowerCase()))

        },
        sortArray() {
            this.transactionsFiltrados.sort((a, b) => a.id - b.id )
        },
    },
    
}).mount('#app');