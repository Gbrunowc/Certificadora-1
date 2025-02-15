PGDMP  (    2                }            BFdb    17.2    17.2     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    16444    BFdb    DATABASE     }   CREATE DATABASE "BFdb" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Portuguese_Brazil.1252';
    DROP DATABASE "BFdb";
                     postgres    false            �            1255    16488    log_voluntario()    FUNCTION     �  CREATE FUNCTION public.log_voluntario() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF OLD.vol_nome <> NEW.vol_nome OR OLD.vol_data <> NEW.vol_data THEN
        INSERT INTO log_voluntario (vol_cpf, vol_nome_original, vol_nome_alterado, vol_data_original, vol_data_alterado)
        VALUES (OLD.vol_cpf, OLD.vol_nome, NEW.vol_nome, OLD.vol_data, NEW.vol_data);
    END IF;
    RETURN NEW;
END;
$$;
 '   DROP FUNCTION public.log_voluntario();
       public               postgres    false            �            1259    16491    disponibilidade    TABLE     �   CREATE TABLE public.disponibilidade (
    id integer NOT NULL,
    vol_cpf bigint,
    dis_dia integer,
    dis_inicio time without time zone,
    dis_fim time without time zone
);
 #   DROP TABLE public.disponibilidade;
       public         heap r       postgres    false            �            1259    16490    disponibilidade_id_seq    SEQUENCE     �   CREATE SEQUENCE public.disponibilidade_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.disponibilidade_id_seq;
       public               postgres    false    221            �           0    0    disponibilidade_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.disponibilidade_id_seq OWNED BY public.disponibilidade.id;
          public               postgres    false    220            �            1259    16480    log_voluntario    TABLE     u  CREATE TABLE public.log_voluntario (
    log_id integer NOT NULL,
    vol_cpf bigint NOT NULL,
    vol_nome_original character varying(30),
    vol_nome_alterado character varying(30),
    vol_data_original date,
    vol_data_alterado date,
    data_alteracao timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    usuario character varying(50) DEFAULT CURRENT_USER
);
 "   DROP TABLE public.log_voluntario;
       public         heap r       postgres    false            �            1259    16479    log_voluntario_log_id_seq    SEQUENCE     �   CREATE SEQUENCE public.log_voluntario_log_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.log_voluntario_log_id_seq;
       public               postgres    false    219            �           0    0    log_voluntario_log_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.log_voluntario_log_id_seq OWNED BY public.log_voluntario.log_id;
          public               postgres    false    218            �            1259    16474 
   voluntario    TABLE     �   CREATE TABLE public.voluntario (
    vol_nome character varying(30) NOT NULL,
    vol_cpf bigint NOT NULL,
    vol_data date NOT NULL
);
    DROP TABLE public.voluntario;
       public         heap r       postgres    false            .           2604    16494    disponibilidade id    DEFAULT     x   ALTER TABLE ONLY public.disponibilidade ALTER COLUMN id SET DEFAULT nextval('public.disponibilidade_id_seq'::regclass);
 A   ALTER TABLE public.disponibilidade ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    220    221    221            +           2604    16483    log_voluntario log_id    DEFAULT     ~   ALTER TABLE ONLY public.log_voluntario ALTER COLUMN log_id SET DEFAULT nextval('public.log_voluntario_log_id_seq'::regclass);
 D   ALTER TABLE public.log_voluntario ALTER COLUMN log_id DROP DEFAULT;
       public               postgres    false    218    219    219            �          0    16491    disponibilidade 
   TABLE DATA           T   COPY public.disponibilidade (id, vol_cpf, dis_dia, dis_inicio, dis_fim) FROM stdin;
    public               postgres    false    221   �       �          0    16480    log_voluntario 
   TABLE DATA           �   COPY public.log_voluntario (log_id, vol_cpf, vol_nome_original, vol_nome_alterado, vol_data_original, vol_data_alterado, data_alteracao, usuario) FROM stdin;
    public               postgres    false    219   �       �          0    16474 
   voluntario 
   TABLE DATA           A   COPY public.voluntario (vol_nome, vol_cpf, vol_data) FROM stdin;
    public               postgres    false    217   �       �           0    0    disponibilidade_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.disponibilidade_id_seq', 30, true);
          public               postgres    false    220            �           0    0    log_voluntario_log_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.log_voluntario_log_id_seq', 1, false);
          public               postgres    false    218            4           2606    16496 $   disponibilidade disponibilidade_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.disponibilidade
    ADD CONSTRAINT disponibilidade_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.disponibilidade DROP CONSTRAINT disponibilidade_pkey;
       public                 postgres    false    221            2           2606    16487 "   log_voluntario log_voluntario_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.log_voluntario
    ADD CONSTRAINT log_voluntario_pkey PRIMARY KEY (log_id);
 L   ALTER TABLE ONLY public.log_voluntario DROP CONSTRAINT log_voluntario_pkey;
       public                 postgres    false    219            0           2606    16478    voluntario voluntario_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.voluntario
    ADD CONSTRAINT voluntario_pkey PRIMARY KEY (vol_cpf);
 D   ALTER TABLE ONLY public.voluntario DROP CONSTRAINT voluntario_pkey;
       public                 postgres    false    217            6           2620    16489 !   voluntario trigger_log_voluntario    TRIGGER        CREATE TRIGGER trigger_log_voluntario AFTER UPDATE ON public.voluntario FOR EACH ROW EXECUTE FUNCTION public.log_voluntario();
 :   DROP TRIGGER trigger_log_voluntario ON public.voluntario;
       public               postgres    false    217    222            5           2606    16497 ,   disponibilidade disponibilidade_vol_cpf_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.disponibilidade
    ADD CONSTRAINT disponibilidade_vol_cpf_fkey FOREIGN KEY (vol_cpf) REFERENCES public.voluntario(vol_cpf);
 V   ALTER TABLE ONLY public.disponibilidade DROP CONSTRAINT disponibilidade_vol_cpf_fkey;
       public               postgres    false    217    4656    221            �   �   x�m�K� е9L��@�����F�PKYD�S�x$��GE@��]�_��wS(�--�B[�P"4�[��a����m�K� ����+#M����`O��	"y��P �F?��y��(�p6��If48�f=�����&c��%%�#����j ��BHKf!�#�\�Ց��O8�$��U8b�����"I���,��@"f��f�.�@���k -���*�|���      �      x������ � �      �   �   x�E�;N�0���s
_`���S.+��X����f�X�R#;�b��Q�6��O�<+�s�oĤ��v��dr
���3?�u�5�
rETN���sX�5�kH��(<�ߟ�_��"k�ԌCѣR���J�D|��/b�*�jc��i���+�f��*k(�{�R��#-�1����*u�,��Bi��0/>LĚr5֣0�Bx���L�-�T���Pc�\W��{����^4�R)����{
���� ���\�     