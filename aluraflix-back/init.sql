BEGIN;

CREATE TABLE IF NOT EXISTS public.video
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    title character varying NOT NULL,
    description character varying NOT NULL,
    url character varying NOT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE public.video
    IS 'Tabela para armazenamento dos dados dos videos';

END;