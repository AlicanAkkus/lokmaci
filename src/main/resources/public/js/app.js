var lokmaci = new Vue({
    el: '#lokmaci',
    data: {
        lokmaci: null,
        gen: 'üret'
    },
    methods: {
        generateLokmaci: function () {
            axios.post('/api/v1/lokmaci')
                .then(function (response) {
                    this.lokmaci = response.data;
                    this.gen = 'yeniden üret';
                }.bind(this))
                .catch(function (error) {
                    console.log(error);
                });
        }
    }
});