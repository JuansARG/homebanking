const { createApp } = Vue;

createApp({
    data(){
        return {
            
        }
    },
    created(){
        this.cargarDatos();
    },
    methods: {
        cargarDatos(){
            axios.post('/api/login',"email=melba@mindhub.com&password=cliente")
                .then(response => console.log('signed in!!!'))
                .catch(e => console.log(e));
        }
    }
}).mount("#app");