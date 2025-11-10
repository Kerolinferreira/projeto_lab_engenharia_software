document.addEventListener('DOMContentLoaded', () => {
    
    // --- Referências do DOM ---
    const form = document.getElementById('produtoForm');
    const submitButton = document.getElementById('submitButton');
    const btnGerar = document.querySelector('.btn-gerar');

    // Campos para a IA
    const inpNome = document.getElementById('nome_produto');
    const inpModelo = document.getElementById('modelo');
    const inpTamanho = document.getElementById('tamanho');
    const inpCor = document.getElementById('cor');
    const txtDescricao = document.getElementById('descricao');

    if (!form || !submitButton) {
        console.error('Formulário ou botão de submit não encontrado. Verifique se o botão "SALVAR PRODUTO" tem o id="submitButton"');
        return;
    }

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        // aqui é só teste de validacao
        if (!form.checkValidity()) {
            form.reportValidity(); 
            return; 
        }

        // formulario
        const formData = new FormData(form);
        const dadosPlanos = Object.fromEntries(formData.entries());
        
        // dto pro java
        const dadosParaApi = {
            nome_produto: dadosPlanos.nome_produto,
            descricao: dadosPlanos.descricao,
            modelo: dadosPlanos.modelo || null, // Trata campo opcional
            qtd_estoque: parseInt(dadosPlanos.qtd_estoque, 10),
            preco: parseFloat(dadosPlanos.preco)
        };

        // desabilita e envia
        submitButton.disabled = true;
        submitButton.textContent = 'Salvando...';

        try {
            // endpoint post
            const response = await fetch('/api/produtos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                cache: 'no-store',
                body: JSON.stringify(dadosParaApi)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || `Erro ${response.status}`);
            }

            // se der certo
            alert('Produto cadastrado com sucesso!');
            form.reset(); // Limpa o formulário

        } catch (error) {
            console.error('Falha ao enviar o formulário:', error);
            alert(`Erro no cadastro: ${error.message}`);
        } finally {
            // volta o botao
            submitButton.disabled = false;
            submitButton.textContent = 'SALVAR PRODUTO';
        }
    });


    // descricao ia
    if (btnGerar) {
        btnGerar.addEventListener('click', async () => {
            
            const nome = inpNome.value;
            const modelo = inpModelo.value;
            const tamanho = inpTamanho.value;
            const cor = inpCor.value;

            if (!nome) {
                alert('Preencha pelo menos o Nome do Produto para gerar a descrição.');
                inpNome.focus();
                return;
            }

            // carregando ficticio
            txtDescricao.value = "Gerando descrição com IA, aguarde...";
            btnGerar.disabled = true;

            // payload da ia
            const payloadIa = { 
                nome_produto: nome,
                modelo: modelo,
                tamanho: tamanho,
                cor: cor
            };

            try {
                // descricao ia post
                const response = await fetch('/api/produtos/gerarDescricao', {
                     method: 'POST',
                     headers: { 'Content-Type': 'application/json' },
                     body: JSON.stringify(payloadIa)
                });
                
                if (!response.ok) {
                    throw new Error(`Falha ao gerar descrição (HTTP ${response.status})`);
                }
                
                const data = await response.json();

                if (data.erro) {
                    throw new Error(data.erro);
                }

                //Pegar a chave correta da resposta ("descricaoGerada")
                const textoGerado = data.descricaoGerada; 
                
                // textarea com variavel do gerado
                txtDescricao.value = textoGerado;


            } catch (error) {
                console.error('Erro ao gerar descrição:', error);
                alert(error.message);
                // Limpa o campo se der erro
                txtDescricao.value = ''; 
            } finally {
                btnGerar.disabled = false;
            }
        });
    }
});