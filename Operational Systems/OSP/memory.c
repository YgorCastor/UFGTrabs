/*===========================================================================*
 *                             Osp de Memória                                *
 *                                                                           *
 *---------------------------------------------------------------------------*/



/****************************************************************************/
/*                                                                          */
/*                           Module MEMORY                                  */
/*                      External Declarations                               */
/*                                                                          */
/****************************************************************************/


/* OSP constants */

#define MAX_PAGE       16                 /* max size of page tables        */
#define MAX_FRAME      32                 /* size of the physical memory    */
#define PAGE_SIZE      512                /* size of a page in bytes        */

#define   COST_OF_PAGE_TRANSFER      6  /* cost of reading page  from drum  */

#include <stdlib.h>


/* external enumeration constants */

typedef enum
{
    false, true                         /* the boolean data type            */
} BOOL;

typedef enum
{
    read, write                         /* type of actions for I/O requests */
} IO_ACTION;

typedef enum
{
    load, store                         /* types of memory reference        */
} REFER_ACTION;

typedef enum
{
    running, ready, waiting, done       /* types of status                  */
} STATUS;

typedef enum
{
    iosvc, devint,                      /* types of interrupt               */
    pagefault, startsvc,
    termsvc, killsvc,
    waitsvc, sigsvc, timeint
} INT_TYPE;



/* external type definitions */

typedef struct page_entry_node PAGE_ENTRY;
typedef struct page_tbl_node PAGE_TBL;
typedef struct event_node EVENT;
typedef struct ofile_node OFILE;
typedef struct pcb_node PCB;
typedef struct iorb_node IORB;
typedef struct int_vector_node INT_VECTOR;
typedef struct frame_node FRAME;



/* external data structures */

extern struct frame_node
{
    BOOL   free;        /* = true, if free                                  */
    PCB    *pcb;        /* process to which the frame currently belongs     */
    int    page_id;     /* vitrual page id - an index to the PCB's page tbl */
    BOOL   dirty;       /* indicates if the frame has been modified         */
    int    lock_count;  /* number of locks set on page involved in an       */
    /* active I/O                                       */
    int    *hook;       /* can hook up anything here                        */
};

extern struct page_entry_node
{
    int    frame_id;    /* frame id holding this page                       */
    BOOL   valid;       /* page in main memory : valid = true; not : false  */
    int    *hook;       /* can hook up anything here                        */
};

extern struct page_tbl_node
{
    PCB    *pcb;        /* PCB of the process in question                   */
    PAGE_ENTRY page_entry[MAX_PAGE];
    int    *hook;       /* can hook up anything here                        */
};

extern struct pcb_node
{
    int    pcb_id;         /* PCB id                                        */
    int    size;           /* process size in bytes; assigned by SIMCORE    */
    int    creation_time;  /* assigned by SIMCORE                           */
    int    last_dispatch;  /* last time the process was dispatched          */
    int    last_cpuburst;  /* length of the previous cpu burst              */
    int    accumulated_cpu;/* accumulated CPU time                          */
    PAGE_TBL *page_tbl;    /* page table associated with the PCB            */
    STATUS status;         /* status of process                             */
    EVENT  *event;         /* event upon which process may be suspended     */
    int    priority;       /* user-defined priority; used for scheduling    */
    PCB    *next;          /* next pcb in whatever queue                    */
    PCB    *prev;          /* previous pcb in whatever queue                */
    int    *hook;          /* can hook up anything here                     */
};

extern struct iorb_node
{
    int    iorb_id;     /* iorb id                                          */
    int    dev_id;      /* associated device; index into the device table   */
    IO_ACTION action;   /* read/write                                       */
    int    block_id;    /* block involved in the I/O                        */
    int    page_id;     /* buffer page in the main memory                   */
    PCB    *pcb;        /* PCB of the process that issued the request       */
    EVENT  *event;      /* event used to synchronize processes with I/O     */
    OFILE  *file;       /* associated entry in the open files table         */
    IORB   *next;       /* next iorb in the device queue                    */
    IORB   *prev;       /* previous iorb in the device queue                */
    int    *hook;       /* can hook up anything here                        */
};

extern struct int_vector_node
{
    INT_TYPE cause;           /* cause of interrupt                         */
    PCB    *pcb;              /* PCB to be started (if startsvc) or pcb that*/
    /* caused page fault (if fagefault interrupt) */
    int    page_id;           /* page causing pagefault                     */
    int    dev_id;            /* device causing devint                      */
    EVENT  *event;            /* event involved in waitsvc and sigsvc calls */
    IORB   *iorb;             /* IORB involved in iosvc call                */
};



/* extern variables */

extern INT_VECTOR Int_Vector;           /* interrupt vector                  */
extern PAGE_TBL *PTBR;                  /* page table base register          */
extern FRAME Frame_Tbl[MAX_FRAME];      /* frame table                       */
extern int Prepage_Degree;              /* global degree of prepaging (0-10) */



/* external routines */

extern siodrum(/* action, pcb, page_id, frame_id */);
/*  IO_ACTION   action;
    PCB         *pcb;
    int         page_id, frame_id;  */
extern int get_clock();
extern gen_int_handler();

void get_page(PCB *pcb, int page_id);




/***************************************************************************/



/****************************************************************************/
/*                                                                          */
/*                                                                          */
/*                              Module MEMORY                               */
/*                            Internal Routines                             */
/*                                                                          */
/*                                                                          */
/****************************************************************************/

/* Estrutura da Fila de Segunda Chance */
typedef struct node
{
    PCB *pcb;
    int Rbit;
    int page_id;
    int frame_id;
    int time;

    struct node *next;
    struct node *prev;
} node;

/* Nós cabeçalho */
typedef struct sFila
{
    node *head, *tail;

} queue;

/* Fila de segunda chance */
queue *g_queue;


void initQueue(queue *g_queue);
queue *newQueue();
char emptyQueue(queue *g_queue);
void enqueue(queue *g_queue, node *pcb);
node *dequeue(queue *g_queue);


queue *newQueue()
{

    queue *new_queue = (queue *) malloc(sizeof(queue));

    if (!new_queue)
        exit(EXIT_FAILURE);
    
    initQueue(new_queue);

    return new_queue;

}


void initQueue(queue *g_queue)
{
    g_queue->head = (node *) malloc (sizeof(node));

    if (!g_queue->head)
        exit(EXIT_FAILURE);
    

    g_queue->tail = (node *) malloc (sizeof(node));

    if (!g_queue->tail)
        exit(EXIT_FAILURE);
    
    g_queue->head->next = g_queue->tail;
    g_queue->tail->prev = g_queue->head;
    g_queue->head->prev = NULL;

    g_queue->tail->next = NULL;
    g_queue->tail->pcb = NULL;
    g_queue->tail->Rbit = 0;
    g_queue->tail->frame_id = -1;
    g_queue->tail->page_id = -1;
    g_queue->tail->time = 0;

    g_queue->head->pcb =  NULL;
    g_queue->head->Rbit = 0;
    g_queue->head->frame_id = -1;
    g_queue->head->page_id = -1;
    g_queue->head->time = 0;

}


char emptyQueue(queue *g_queue)
{
    return (g_queue->head->next == g_queue->tail);
}

void enqueue(queue *g_queue, node *pcb)
{
    node *tail = g_queue->tail;
    pcb->prev = tail->prev;
    pcb->next = tail;
    tail->prev = pcb;
    pcb->prev->next = pcb;

}


node *dequeue(queue *g_queue)
{
    if (emptyQueue(g_queue))
        return NULL;

    node *p = g_queue->head->next;

    g_queue->head->next = g_queue->head->next->next;
    g_queue->head->next->prev = g_queue->head;

    p->next = NULL;
    p->prev = NULL;

    return p;

}

void memory_init()
{
    g_queue = newQueue();

}


void prepage(PCB *pcb)
{


}

int start_cost(PCB *pcb)
{

}


void deallocate(PCB *pcb)
{
    int i, frame_id;
    node *p;
    for (i = 0; i < MAX_PAGE; i++)
    {

        if (pcb->page_tbl->page_entry[i].valid)
        {

            p = g_queue->head->next;

            frame_id = pcb->page_tbl->page_entry[i].frame_id;
            Frame_Tbl[frame_id].free = true;
            Frame_Tbl[frame_id].pcb = NULL;
            Frame_Tbl[frame_id].page_id = -1;
            Frame_Tbl[frame_id].dirty = false;

            while (p->frame_id != frame_id)
                p = p->next;

            p->prev->next = p->next;
            p->next->prev = p->prev;
            p->next = NULL;
            p->prev = NULL;

            free(p);
        }
    }
    
}

void get_page(PCB *pcb, int page_id)
{
    node *page, *aux;

    int i, j, frame_idx = 0 , oldest_page_time = 0;
    char free_frame = false;

    /*Busca por um frame livre*/
    for (i = 0; i < MAX_FRAME; i++)
    {
        if (Frame_Tbl[i].free && !Frame_Tbl[i].lock_count)
        {
            free_frame = true; // Se encontrou um frame livre, seta a flag para válida.
            break;
        }
    }

    frame_idx = i;

    /* Se não encontrou uma frame válida, deve-se remover uma das páginas alocadas e colocar no disco */
    if (!free_frame)
    {
        page = g_queue->head->next;
        node *oldest_page = page;

        /* Percorre toda a fila em busca de uma página */
        while ( ( aux = page->next ) != NULL )
        {
            aux = page->next;
            if (!Frame_Tbl[page->frame_id].lock_count)
            {
                
                /* Checa o Bit de Referência, se ele for 1, decresce e reinsere a fila */
                if (page->Rbit)
                {
                    page->Rbit = false;

                    page->prev->next = page->next;
                    page->next->prev = page->prev;
                    page->next = NULL;
                    page->prev = NULL;
                    enqueue(g_queue, page);
                    page->time = get_clock(); // A página entra como se fosse nova.

                }
                else
                {

                    /* Busca a página mais antiga cujo bit de referência seja 0 */
                    if (oldest_page_time < page->time )
                    {
                        oldest_page_time = page->time;
                        oldest_page = page;

                    }


                }
            }

            page = aux;

        }


        /* Encontrada a última página, se ela estiver dirty, ou seja, esteja sendo utilizada
           por algum processo, ela deve ser escrita no disco */
        if (Frame_Tbl[oldest_page->frame_id].dirty)
            siodrum(write, oldest_page->pcb, oldest_page->page_id, oldest_page->frame_id);

        oldest_page->pcb->page_tbl->page_entry[oldest_page->page_id].valid = false;

        oldest_page->Rbit = true;
        oldest_page->page_id = page_id;
        oldest_page->pcb = pcb;
        oldest_page->time = get_clock();

        frame_idx = oldest_page->frame_id;

    }

    
    /* Torna a página válida para escrita */
    pcb->page_tbl->page_entry[page_id].frame_id = frame_idx;
    pcb->page_tbl->page_entry[page_id].valid = true;
    Frame_Tbl[frame_idx].pcb = pcb;
    Frame_Tbl[frame_idx].page_id = page_id;
    Frame_Tbl[frame_idx].dirty = false;

    /* Flag caso tenha encontrado um frame vazio */
    if (free_frame)
    {

        Frame_Tbl[frame_idx].free = false;

        page = (node *) malloc(sizeof(node));
        if (!page) exit(1);

        page->page_id = page_id;
        page->pcb = pcb;
        page->frame_id = frame_idx;
        page->Rbit = true;
        page->time = get_clock();

        enqueue(g_queue, page);

    }

    /* Lê a página do disco */
    siodrum(read, pcb, page_id, frame_idx);

}


/* Trava uma página por requisição do Sistema de I/O */
void lock_page(IORB *iorb)
{
    int frame_id, page_id;
    PCB *pcb;
    pcb = iorb->pcb;
    page_id = iorb->page_id;


    if (!pcb->page_tbl->page_entry[page_id].valid)
    {
        Int_Vector.cause = pagefault;
        Int_Vector.page_id = page_id;
        Int_Vector.pcb = pcb;
        gen_int_handler();

    }

    frame_id = pcb->page_tbl->page_entry[page_id].frame_id;

    if (iorb->action == read)
        Frame_Tbl[frame_id].dirty = true;

    Frame_Tbl[frame_id].lock_count++;

}

/* Destrava uma página por requisição do Sistema de I/O */
void unlock_page(IORB *iorb)
{
    int frame_id;
    int page_id;
    PCB *pcb;
    pcb = iorb->pcb;
    page_id = iorb->page_id;
    frame_id = pcb->page_tbl->page_entry[page_id].frame_id;
    Frame_Tbl[frame_id].lock_count--;
}



/* O Refer simula requisições de página por parte dos processos.*/
void refer(int logic_addr, REFER_ACTION action)
{
    int page_id, frame_id;
    PCB *pcb;
    node *p;
    pcb = PTBR->pcb;

    page_id = logic_addr / PAGE_SIZE;

    if (!pcb->page_tbl->page_entry[page_id].valid)
    {
        Int_Vector.cause = pagefault;
        Int_Vector.page_id = page_id;
        Int_Vector.pcb = pcb;
        gen_int_handler();

    }

    frame_id = pcb->page_tbl->page_entry[page_id].frame_id;

    p = g_queue->head->next ;

    while (p->frame_id != frame_id)
        p = p->next;

    p->Rbit = true;
    p->time = get_clock();
    if (action == store)
        Frame_Tbl[frame_id].dirty = true;
    
}

/* end of module */
