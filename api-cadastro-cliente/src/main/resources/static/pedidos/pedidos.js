document.addEventListener('DOMContentLoaded', () => {

    // --- Referências do DOM ---
    const form = document.getElementById('pedidoForm');
    const itensTable = document.getElementById('itensTable');
    const tbody = itensTable.querySelector('tbody');
    const thead = itensTable.querySelector('thead');

    // Botões
    const btnBuscarCliente = document.getElementById('btnBuscarCliente');
    const btnAddItem = document.getElementById('addItemBtn');
    const btnClearItems = document.getElementById('clearItemsBtn');

    // Campos do pedido
    const inputNumero = document.getElementById('numero');
    const inputDataPedido = document.getElementById('data_pedido');
    const inputClienteBusca = document.getElementById('cliente_busca');
    const inputClienteId = document.getElementById('cliente_id');
    const inputClienteRazao = document.getElementById('cliente_razao');
    const inputClienteFantasia = document.getElementById('cliente_fantasia');
    const inputClienteCnpj = document.getElementById('cliente_cnpj');
    const inputClienteCidade = document.getElementById('cliente_cidade');

    // Campos de totais
    const inputQtdPadrao = document.getElementById('qtd_padrao');
    const inputDesconto = document.getElementById('desconto');
    const inputFrete = document.getElementById('frete');
    const inputTotal = document.getElementById('total');

    // Modal de produtos
    const modalProdutos = document.getElementById('modalProdutos');
    const btnFecharModal = document.getElementById('fecharModal');
    const btnCancelarProduto = document.getElementById('cancelarProduto');
    const inputBuscaProduto = document.getElementById('buscaProduto');

    // Array para armazenar os itens do pedido
    let itensCarrinho = [];
    let clienteSelecionado = null;

    // --- Inicialização ---
    inicializarTabela();
    carregarProximoNumero();
    setDataAtual();

    // --- Event Listeners ---
    btnBuscarCliente.addEventListener('click', buscarCliente);
    btnAddItem.addEventListener('click', abrirModalProdutos);
    btnClearItems.addEventListener('click', limparItens);
    btnFecharModal.addEventListener('click', fecharModal);
    btnCancelarProduto.addEventListener('click', fecharModal);
    form.addEventListener('submit', salvarPedido);

    // Recalcular total quando desconto ou frete mudar
    inputDesconto.addEventListener('input', calcularTotal);
    inputFrete.addEventListener('input', calcularTotal);

    // Busca de produto no modal
    inputBuscaProduto.addEventListener('input', () => {
        const termo = inputBuscaProduto.value;
        if (termo.length >= 2) {
            buscarProdutos(termo);
        }
    });

    // --- Funções ---

    function inicializarTabela() {
        thead.innerHTML = `
            <tr>
                <th style="width:80px">ID</th>
                <th>Produto</th>
                <th style="width:120px">Preço Unit.</th>
                <th style="width:100px">Qtd</th>
                <th style="width:120px">Subtotal</th>
                <th style="width:80px">Ações</th>
            </tr>
        `;
    }

    async function carregarProximoNumero() {
        try {
            const response = await fetch('/api/pedidos/ultimoId');
            const ultimoId = await response.json();
            inputNumero.value = ultimoId + 1;
        } catch (error) {
            console.error('Erro ao carregar próximo número:', error);
            inputNumero.value = '1';
        }
    }

    function setDataAtual() {
        const hoje = new Date().toISOString().split('T')[0];
        inputDataPedido.value = hoje;
    }

    async function buscarCliente() {
        const termo = inputClienteBusca.value.trim();

        if (!termo) {
            alert('Digite algo para buscar o cliente');
            return;
        }

        try {
            const response = await fetch(`/api/clientes/buscarPorTexto?texto=${encodeURIComponent(termo)}`);

            if (!response.ok) {
                throw new Error('Erro ao buscar cliente');
            }

            const clientes = await response.json();

            if (clientes.length === 0) {
                alert('Nenhum cliente encontrado');
                return;
            }

            // Se encontrou apenas 1, seleciona automaticamente
            if (clientes.length === 1) {
                selecionarCliente(clientes[0]);
            } else {
                // Se encontrou mais de 1, mostra lista para escolher
                mostrarListaClientes(clientes);
            }

        } catch (error) {
            console.error('Erro ao buscar cliente:', error);
            alert('Erro ao buscar cliente: ' + error.message);
        }
    }

    function mostrarListaClientes(clientes) {
        const opcoes = clientes.map((c, i) =>
            `${i + 1}. ${c.nome} - ${c.cpf_cnpj} - ${c.endereco?.cidade || ''}`
        ).join('\n');

        const escolha = prompt(`Encontrados ${clientes.length} clientes:\n\n${opcoes}\n\nDigite o número do cliente:`);

        if (escolha) {
            const index = parseInt(escolha) - 1;
            if (index >= 0 && index < clientes.length) {
                selecionarCliente(clientes[index]);
            }
        }
    }

    function selecionarCliente(cliente) {
        clienteSelecionado = cliente;
        inputClienteId.value = cliente.id;
        inputClienteRazao.value = cliente.nome;
        inputClienteFantasia.value = cliente.nome_fantasia || '';
        inputClienteCnpj.value = cliente.cpf_cnpj;
        inputClienteCidade.value = cliente.endereco?.cidade || '';
    }

    function abrirModalProdutos() {
        modalProdutos.showModal();
        inputBuscaProduto.value = '';
        carregarTodosProdutos();
    }

    function fecharModal() {
        modalProdutos.close();
    }

    async function carregarTodosProdutos() {
        try {
            const response = await fetch('/api/produtos/listarProdutos');
            const produtos = await response.json();
            exibirProdutosNoModal(produtos);
        } catch (error) {
            console.error('Erro ao carregar produtos:', error);
        }
    }

    async function buscarProdutos(termo) {
        try {
            const response = await fetch(`/api/produtos/buscarportexto/?texto=${encodeURIComponent(termo)}`);
            const produtos = await response.json();
            exibirProdutosNoModal(produtos);
        } catch (error) {
            console.error('Erro ao buscar produtos:', error);
        }
    }

    function exibirProdutosNoModal(produtos) {
        const tabelaProdutos = modalProdutos.querySelector('tbody');

        if (!tabelaProdutos) {
            // Cria o tbody se não existir
            const table = modalProdutos.querySelector('table');
            const tbody = document.createElement('tbody');
            tbody.id = 'tabelaProdutos';
            table.appendChild(tbody);
            exibirProdutosNoModal(produtos);
            return;
        }

        tabelaProdutos.innerHTML = '';

        produtos.forEach(produto => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${produto.id}</td>
                <td>${produto.nome_produto}</td>
                <td>${produto.descricao || ''}</td>
                <td>R$ ${produto.preco.toFixed(2)}</td>
                <td>
                    <button type="button" class="btn-add" data-produto='${JSON.stringify(produto)}'>
                        Adicionar
                    </button>
                </td>
            `;

            const btnAdicionar = tr.querySelector('.btn-add');
            btnAdicionar.addEventListener('click', () => {
                adicionarItemAoPedido(produto);
                fecharModal();
            });

            tabelaProdutos.appendChild(tr);
        });
    }

    function adicionarItemAoPedido(produto) {
        // Verifica se o produto já está no carrinho
        const itemExistente = itensCarrinho.find(item => item.produtoId === produto.id);

        if (itemExistente) {
            // Se já existe, incrementa a quantidade
            itemExistente.quantidade += parseInt(inputQtdPadrao.value);
            itemExistente.subtotal = itemExistente.quantidade * itemExistente.precoUnitario;
        } else {
            // Se não existe, adiciona novo item
            const novoItem = {
                produtoId: produto.id,
                nomeProduto: produto.nome_produto,
                quantidade: parseInt(inputQtdPadrao.value),
                precoUnitario: produto.preco,
                subtotal: parseInt(inputQtdPadrao.value) * produto.preco
            };
            itensCarrinho.push(novoItem);
        }

        renderizarItens();
        calcularTotal();
    }

    function renderizarItens() {
        tbody.innerHTML = '';

        itensCarrinho.forEach((item, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${item.produtoId}</td>
                <td>${item.nomeProduto}</td>
                <td>R$ ${item.precoUnitario.toFixed(2)}</td>
                <td>
                    <input type="number" min="1" value="${item.quantidade}"
                           class="input-qtd" data-index="${index}" style="width:80px;">
                </td>
                <td>R$ ${item.subtotal.toFixed(2)}</td>
                <td>
                    <button type="button" class="btn-del" data-index="${index}">X</button>
                </td>
            `;

            // Event listener para alterar quantidade
            const inputQtd = tr.querySelector('.input-qtd');
            inputQtd.addEventListener('change', (e) => {
                const novaQtd = parseInt(e.target.value);
                if (novaQtd > 0) {
                    item.quantidade = novaQtd;
                    item.subtotal = item.quantidade * item.precoUnitario;
                    renderizarItens();
                    calcularTotal();
                }
            });

            // Event listener para remover item
            const btnRemover = tr.querySelector('.btn-del');
            btnRemover.addEventListener('click', () => {
                itensCarrinho.splice(index, 1);
                renderizarItens();
                calcularTotal();
            });

            tbody.appendChild(tr);
        });
    }

    function calcularTotal() {
        const subtotal = itensCarrinho.reduce((sum, item) => sum + item.subtotal, 0);
        const desconto = parseFloat(inputDesconto.value) || 0;
        const frete = parseFloat(inputFrete.value) || 0;
        const total = subtotal - desconto + frete;

        inputTotal.value = total.toFixed(2).replace('.', ',');
    }

    function limparItens() {
        if (confirm('Deseja realmente remover todos os itens?')) {
            itensCarrinho = [];
            renderizarItens();
            calcularTotal();
        }
    }

    async function salvarPedido(event) {
        event.preventDefault();

        // Validações
        if (!clienteSelecionado) {
            alert('Selecione um cliente antes de salvar o pedido');
            inputClienteBusca.focus();
            return;
        }

        if (itensCarrinho.length === 0) {
            alert('Adicione pelo menos um item ao pedido');
            return;
        }

        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }

        // Monta o objeto do pedido
        const pedidoData = {
            dataPedido: inputDataPedido.value,
            clienteId: clienteSelecionado.id,
            itens: itensCarrinho,
            desconto: parseFloat(inputDesconto.value) || 0,
            frete: parseFloat(inputFrete.value) || 0,
            formaPagamento: document.getElementById('forma_pagamento').value || null,
            condicaoPagamento: document.getElementById('condicao_pagamento').value || null,
            previsaoEntrega: document.getElementById('previsao_entrega').value || null,
            observacoes: document.getElementById('observacoes').value || null
        };

        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.disabled = true;
        submitBtn.textContent = 'Salvando...';

        try {
            const response = await fetch('/api/pedidos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(pedidoData)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || `Erro ${response.status}`);
            }

            const pedidoSalvo = await response.json();

            alert(`Pedido #${pedidoSalvo.id} cadastrado com sucesso!`);

            // Limpa o formulário
            form.reset();
            itensCarrinho = [];
            clienteSelecionado = null;
            renderizarItens();
            calcularTotal();
            carregarProximoNumero();
            setDataAtual();

            // Limpa os campos do cliente
            inputClienteId.value = '';
            inputClienteRazao.value = '';
            inputClienteFantasia.value = '';
            inputClienteCnpj.value = '';
            inputClienteCidade.value = '';

        } catch (error) {
            console.error('Erro ao salvar pedido:', error);
            alert(`Erro ao salvar pedido: ${error.message}`);
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = 'SALVAR PEDIDO';
        }
    }
});
