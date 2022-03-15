DROP TABLE IF EXISTS public.productos;
CREATE TABLE public.productos
(
    id_producto varchar(255) NOT NULL DEFAULT RANDOM_UUID(),
    created_at  timestamp         NULL,
    nombre      varchar(255) NULL,
    precio      float8       NULL,
    CONSTRAINT productos_pkey PRIMARY KEY (id_producto)
);

INSERT INTO public.productos (ID_PRODUCTO, NOMBRE, PRECIO, CREATED_AT) VALUES(RANDOM_UUID(),'Panasonic', 800, NOW());
INSERT INTO public.productos (ID_PRODUCTO, NOMBRE, PRECIO, CREATED_AT) VALUES(RANDOM_UUID(),'Sony', 700, NOW());
INSERT INTO public.productos (ID_PRODUCTO, NOMBRE, PRECIO, CREATED_AT) VALUES(RANDOM_UUID(),'Apple', 1000, NOW());
INSERT INTO public.productos (ID_PRODUCTO, NOMBRE, PRECIO, CREATED_AT) VALUES(RANDOM_UUID(),'Sony Notebook', 1000, NOW());
INSERT INTO public.productos (ID_PRODUCTO, NOMBRE, PRECIO, CREATED_AT) VALUES(RANDOM_UUID(),'Hewlett Packard', 500, NOW());
INSERT INTO public.productos (ID_PRODUCTO, NOMBRE, PRECIO, CREATED_AT) VALUES(RANDOM_UUID(),'Bianchi', 600, NOW());
INSERT INTO public.productos (ID_PRODUCTO, NOMBRE, PRECIO, CREATED_AT) VALUES(RANDOM_UUID(),'Nike', 100, NOW());
INSERT INTO public.productos (ID_PRODUCTO, NOMBRE, PRECIO, CREATED_AT) VALUES(RANDOM_UUID(),'Adidas', 200, NOW());
INSERT INTO public.productos (ID_PRODUCTO, NOMBRE, PRECIO, CREATED_AT) VALUES(RANDOM_UUID(),'Reebok', 300, NOW());
