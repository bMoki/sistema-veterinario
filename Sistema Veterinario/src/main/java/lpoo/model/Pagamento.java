package lpoo.model;

public enum Pagamento {
    CARTAO_DEBITO,
    CARTAO_CREDITO,
    DINHEIRO,
    PIX,
    BOLETO;

    public static Pagamento getPagamento(String nameEnum) {
        if(nameEnum.equals(Pagamento.CARTAO_CREDITO.toString()))

            return Pagamento.CARTAO_CREDITO;

        else if(nameEnum.equals(Pagamento.CARTAO_DEBITO.toString())){

            return Pagamento.CARTAO_DEBITO;

        } else if(nameEnum.equals(Pagamento.DINHEIRO.toString())){

            return Pagamento.DINHEIRO;

        } else if(nameEnum.equals(Pagamento.BOLETO.toString())){

            return Pagamento.BOLETO;

        } else if(nameEnum.equals(Pagamento.PIX.toString())){

            return Pagamento.PIX;

        }else{
            return null;
        }
    }
}
